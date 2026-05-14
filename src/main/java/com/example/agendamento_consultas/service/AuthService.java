package com.example.agendamento_consultas.service;

import com.example.agendamento_consultas.database.enums.Role;
import com.example.agendamento_consultas.database.model.AuthAuditLog;
import com.example.agendamento_consultas.database.model.RefreshToken;
import com.example.agendamento_consultas.database.model.RevokedAccessToken;
import com.example.agendamento_consultas.database.model.Usuario;
import com.example.agendamento_consultas.database.repository.AuthAuditLogRepository;
import com.example.agendamento_consultas.database.repository.RefreshTokenRepository;
import com.example.agendamento_consultas.database.repository.RevokedAccessTokenRepository;
import com.example.agendamento_consultas.database.repository.UsuarioRepository;
import com.example.agendamento_consultas.dto.request.LoginRequest;
import com.example.agendamento_consultas.dto.request.RefreshTokenRequest;
import com.example.agendamento_consultas.dto.request.RegisterRequest;
import com.example.agendamento_consultas.dto.response.LoginResponse;
import com.example.agendamento_consultas.dto.response.RegisterResponse;
import com.example.agendamento_consultas.exception.BusinessException;
import com.example.agendamento_consultas.exception.ResourceAlreadyExistsException;
import com.example.agendamento_consultas.security.JwtService;
import com.example.agendamento_consultas.security.TokenHashService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RevokedAccessTokenRepository revokedAccessTokenRepository;
    private final AuthAuditLogRepository authAuditLogRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenHashService tokenHashService;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpirationMillis;

    @Value("${app.auth.bootstrap-admin-key:}")
    private String bootstrapAdminKey;

    @Transactional
    public RegisterResponse register(RegisterRequest request, HttpServletRequest servletRequest) {
        Usuario salvo = createUser(request, Set.of(Role.ROLE_MEDICO), "REGISTER", servletRequest);
        return new RegisterResponse(salvo.getId(), salvo.getEmail());
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public RegisterResponse bootstrapAdmin(RegisterRequest request, String bootstrapKey, HttpServletRequest servletRequest) {
        if (usuarioRepository.existsByRolesContaining(Role.ROLE_ADMIN)) {
            audit("BOOTSTRAP_ADMIN", request.email(), false, servletRequest, "Administrador ja cadastrado");
            throw new ResourceAlreadyExistsException("Administrador ja cadastrado");
        }

        if (!StringUtils.hasText(bootstrapAdminKey) || !bootstrapAdminKey.equals(bootstrapKey)) {
            audit("BOOTSTRAP_ADMIN", request.email(), false, servletRequest, "Bootstrap key invalida");
            throw new BusinessException("Bootstrap key invalida");
        }

        Usuario salvo = createUser(request, Set.of(Role.ROLE_ADMIN), "BOOTSTRAP_ADMIN", servletRequest);
        return new RegisterResponse(salvo.getId(), salvo.getEmail());
    }

    private Usuario createUser(RegisterRequest request, Set<Role> roles, String action, HttpServletRequest servletRequest) {
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            audit(action, request.email(), false, servletRequest, "Email ja cadastrado");
            throw new ResourceAlreadyExistsException("Email ja cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setRoles(roles);

        Usuario salvo = usuarioRepository.save(usuario);
        audit(action, salvo.getEmail(), true, servletRequest, null);
        return salvo;
    }

    @Transactional
    public LoginResponse login(LoginRequest request, HttpServletRequest servletRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.senha()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new AuthenticationServiceException("Usuario autenticado nao encontrado"));

            String token = jwtService.gerarToken(userDetails);
            String refreshToken = createRefreshToken(usuario);
            audit("LOGIN", usuario.getEmail(), true, servletRequest, null);

            return new LoginResponse(token, refreshToken, "Bearer", jwtService.getExpirationMillis());
        } catch (RuntimeException ex) {
            audit("LOGIN", request.email(), false, servletRequest, "Credenciais invalidas");
            throw ex;
        }
    }

    @Transactional
    public LoginResponse refresh(RefreshTokenRequest request, HttpServletRequest servletRequest) {
        String tokenHash = tokenHashService.hash(request.refreshToken());
        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new BusinessException("Refresh token invalido"));

        if (!refreshToken.isActive()) {
            audit("REFRESH", refreshToken.getUsuario().getEmail(), false, servletRequest, "Refresh token expirado ou revogado");
            throw new BusinessException("Refresh token expirado ou revogado");
        }

        Usuario usuario = refreshToken.getUsuario();
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(usuario.getEmail())
                .password(usuario.getSenha())
                .authorities(usuario.getRoles().stream().map(Enum::name).toArray(String[]::new))
                .build();

        refreshToken.setRevokedAt(Instant.now());
        refreshTokenRepository.save(refreshToken);

        String accessToken = jwtService.gerarToken(userDetails);
        String newRefreshToken = createRefreshToken(usuario);
        audit("REFRESH", usuario.getEmail(), true, servletRequest, null);

        return new LoginResponse(accessToken, newRefreshToken, "Bearer", jwtService.getExpirationMillis());
    }

    @Transactional
    public void logout(String authorizationHeader, String email, HttpServletRequest servletRequest) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Usuario nao encontrado"));

        Instant now = Instant.now();
        refreshTokenRepository.findByUsuarioAndRevokedAtIsNull(usuario)
                .forEach(refreshToken -> {
                    refreshToken.setRevokedAt(now);
                    refreshTokenRepository.save(refreshToken);
                });

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            RevokedAccessToken revokedAccessToken = new RevokedAccessToken();
            revokedAccessToken.setTokenHash(tokenHashService.hash(token));
            revokedAccessToken.setExpiresAt(jwtService.extrairExpiracao(token));
            revokedAccessToken.setRevokedAt(now);
            revokedAccessTokenRepository.save(revokedAccessToken);
        }

        audit("LOGOUT", email, true, servletRequest, null);
    }

    private String createRefreshToken(Usuario usuario) {
        String rawToken = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setTokenHash(tokenHashService.hash(rawToken));
        refreshToken.setUsuario(usuario);
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setExpiresAt(Instant.now().plusMillis(refreshExpirationMillis));
        refreshTokenRepository.save(refreshToken);
        return rawToken;
    }

    private void audit(String action, String email, boolean success, HttpServletRequest request, String reason) {
        AuthAuditLog auditLog = new AuthAuditLog();
        auditLog.setAction(action);
        auditLog.setEmail(email);
        auditLog.setSuccess(success);
        auditLog.setIpAddress(request.getRemoteAddr());
        auditLog.setUserAgent(request.getHeader("User-Agent"));
        auditLog.setReason(reason);
        auditLog.setCreatedAt(Instant.now());
        authAuditLogRepository.save(auditLog);
    }
}
