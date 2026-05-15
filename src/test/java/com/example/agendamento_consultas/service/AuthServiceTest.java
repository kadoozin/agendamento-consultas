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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private RevokedAccessTokenRepository revokedAccessTokenRepository;

    @Mock
    private AuthAuditLogRepository authAuditLogRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private TokenHashService tokenHashService;

    @Mock
    private HttpServletRequest servletRequest;

    @InjectMocks
    private AuthService authService;

    @Test
    void deveRegistrarUsuarioQuandoEmailAindaNaoExiste() {
        RegisterRequest request = new RegisterRequest("medico@medico.com", "super123");
        Usuario usuarioSalvo = new Usuario(1L, request.email(), "senhacriptografada", Set.of(Role.ROLE_MEDICO));

        ReflectionTestUtils.setField(authService, "refreshExpirationMillis", 604800000L);
        mockRequestMetadata();
        when(usuarioRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.senha())).thenReturn("senhacriptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        RegisterResponse response = authService.register(request, servletRequest);

        assertEquals(1L, response.id());
        assertEquals(request.email(), response.email());

        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(usuarioCaptor.capture());
        assertEquals(Set.of(Role.ROLE_MEDICO), usuarioCaptor.getValue().getRoles());
        verify(authAuditLogRepository).save(any(AuthAuditLog.class));
    }

    @Test
    void deveImpedirRegistroQuandoEmailJaExiste() {
        RegisterRequest request = new RegisterRequest("medico@medico.com", "super123");

        mockRequestMetadata();
        when(usuarioRepository.findByEmail(request.email())).thenReturn(Optional.of(new Usuario()));

        assertThrows(ResourceAlreadyExistsException.class, () -> authService.register(request, servletRequest));

        verify(usuarioRepository, never()).save(any());
        verify(authAuditLogRepository).save(any(AuthAuditLog.class));
    }

    @Test
    void deveCriarPrimeiroAdminQuandoBootstrapKeyForValida() {
        RegisterRequest request = new RegisterRequest("admin@clinica.com", "super123");
        Usuario adminSalvo = new Usuario(2L, request.email(), "senhacriptografada", Set.of(Role.ROLE_ADMIN));

        mockRequestMetadata();
        ReflectionTestUtils.setField(authService, "bootstrapAdminEnabled", true);
        ReflectionTestUtils.setField(authService, "bootstrapAdminKey", "bootstrap-secreto");
        when(usuarioRepository.existsByRolesContaining(Role.ROLE_ADMIN)).thenReturn(false);
        when(usuarioRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.senha())).thenReturn("senhacriptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(adminSalvo);

        RegisterResponse response = authService.bootstrapAdmin(request, "bootstrap-secreto", servletRequest);

        assertEquals(2L, response.id());
        assertEquals(request.email(), response.email());

        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(usuarioCaptor.capture());
        assertEquals(Set.of(Role.ROLE_ADMIN), usuarioCaptor.getValue().getRoles());
        verify(authAuditLogRepository).save(any(AuthAuditLog.class));
    }

    @Test
    void deveImpedirBootstrapQuandoJaExisteAdmin() {
        RegisterRequest request = new RegisterRequest("admin@clinica.com", "super123");

        mockRequestMetadata();
        ReflectionTestUtils.setField(authService, "bootstrapAdminEnabled", true);
        when(usuarioRepository.existsByRolesContaining(Role.ROLE_ADMIN)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () ->
                authService.bootstrapAdmin(request, "bootstrap-secreto", servletRequest));

        verify(usuarioRepository, never()).save(any());
        verify(authAuditLogRepository).save(any(AuthAuditLog.class));
    }

    @Test
    void deveImpedirBootstrapQuandoChaveForInvalida() {
        RegisterRequest request = new RegisterRequest("admin@clinica.com", "super123");

        mockRequestMetadata();
        ReflectionTestUtils.setField(authService, "bootstrapAdminEnabled", true);
        ReflectionTestUtils.setField(authService, "bootstrapAdminKey", "bootstrap-secreto");
        when(usuarioRepository.existsByRolesContaining(Role.ROLE_ADMIN)).thenReturn(false);

        assertThrows(BusinessException.class, () ->
                authService.bootstrapAdmin(request, "chave-errada", servletRequest));

        verify(usuarioRepository, never()).save(any());
        verify(authAuditLogRepository).save(any(AuthAuditLog.class));
    }

    @Test
    void deveImpedirBootstrapQuandoRecursoEstiverDesabilitado() {
        RegisterRequest request = new RegisterRequest("admin@clinica.com", "super123");

        mockRequestMetadata();
        ReflectionTestUtils.setField(authService, "bootstrapAdminEnabled", false);

        assertThrows(BusinessException.class, () ->
                authService.bootstrapAdmin(request, "qualquer-chave", servletRequest));

        verify(usuarioRepository, never()).save(any());
        verify(authAuditLogRepository).save(any(AuthAuditLog.class));
    }

    @Test
    void deveGerarAccessTokenERefreshTokenNoLogin() {
        LoginRequest request = new LoginRequest("medico@medico.com", "super123");
        User userDetails = new User(request.email(), "senhacriptografada", List.of());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        Usuario usuario = new Usuario(5L, request.email(), "senhacriptografada", Set.of(Role.ROLE_MEDICO));

        ReflectionTestUtils.setField(authService, "refreshExpirationMillis", 604800000L);
        mockRequestMetadata();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(usuarioRepository.findByEmail(request.email())).thenReturn(Optional.of(usuario));
        when(jwtService.gerarToken(userDetails)).thenReturn("jwt-access");
        when(jwtService.getExpirationMillis()).thenReturn(86400000L);
        when(tokenHashService.hash(any(String.class))).thenReturn("refresh-token-hash");

        LoginResponse response = authService.login(request, servletRequest);

        assertEquals("jwt-access", response.token());
        assertEquals("Bearer", response.tokenType());
        assertEquals(86400000L, response.expiresIn());
        assertNotNull(response.refreshToken());

        ArgumentCaptor<RefreshToken> refreshTokenCaptor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(refreshTokenCaptor.capture());
        assertEquals(usuario, refreshTokenCaptor.getValue().getUsuario());
        verify(authAuditLogRepository).save(any(AuthAuditLog.class));
    }

    @Test
    void deveAuditarFalhaQuandoLoginForInvalido() {
        LoginRequest request = new LoginRequest("medico@medico.com", "senhaincorreta");

        mockRequestMetadata();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciais invalidas"));

        assertThrows(BadCredentialsException.class, () -> authService.login(request, servletRequest));

        verify(refreshTokenRepository, never()).save(any());
        verify(authAuditLogRepository).save(any(AuthAuditLog.class));
    }

    @Test
    void deveRejeitarRefreshTokenRevogadoOuExpirado() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsuario(new Usuario(7L, "medico@medico.com", "senha", Set.of(Role.ROLE_MEDICO)));
        refreshToken.setExpiresAt(Instant.now().plusSeconds(300));
        refreshToken.setRevokedAt(Instant.now());

        mockRequestMetadata();
        when(tokenHashService.hash("refresh-token")).thenReturn("refresh-token-hash");
        when(refreshTokenRepository.findByTokenHash("refresh-token-hash")).thenReturn(Optional.of(refreshToken));

        assertThrows(BusinessException.class, () -> authService.refresh(
                new RefreshTokenRequest("refresh-token"),
                servletRequest
        ));

        verify(authAuditLogRepository).save(any(AuthAuditLog.class));
    }

    @Test
    void deveRevogarRefreshTokensEAccessTokenNoLogout() {
        Usuario usuario = new Usuario(9L, "medico@medico.com", "senha", Set.of(Role.ROLE_MEDICO));
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsuario(usuario);
        refreshToken.setExpiresAt(Instant.now().plusSeconds(300));
        String authorizationHeader = "Bearer jwt-access";
        Instant expiracao = Instant.now().plusSeconds(3600);

        mockRequestMetadata();
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(refreshTokenRepository.findByUsuarioAndRevokedAtIsNull(usuario)).thenReturn(List.of(refreshToken));
        when(tokenHashService.hash("jwt-access")).thenReturn("jwt-access-hash");
        when(jwtService.extrairExpiracao("jwt-access")).thenReturn(expiracao);

        authService.logout(authorizationHeader, usuario.getEmail(), servletRequest);

        assertNotNull(refreshToken.getRevokedAt());

        ArgumentCaptor<RevokedAccessToken> revokedTokenCaptor = ArgumentCaptor.forClass(RevokedAccessToken.class);
        verify(revokedAccessTokenRepository).save(revokedTokenCaptor.capture());
        assertEquals("jwt-access-hash", revokedTokenCaptor.getValue().getTokenHash());
        assertEquals(expiracao, revokedTokenCaptor.getValue().getExpiresAt());
        verify(authAuditLogRepository).save(any(AuthAuditLog.class));
    }

    private void mockRequestMetadata() {
        when(servletRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        when(servletRequest.getHeader("User-Agent")).thenReturn("JUnit");
    }
}
