package com.example.agendamento_consultas.controller;

import com.example.agendamento_consultas.config.OpenApiConfig;
import com.example.agendamento_consultas.dto.request.LoginRequest;
import com.example.agendamento_consultas.dto.request.RefreshTokenRequest;
import com.example.agendamento_consultas.dto.request.RegisterRequest;
import com.example.agendamento_consultas.dto.response.ApiErrorResponse;
import com.example.agendamento_consultas.dto.response.LoginResponse;
import com.example.agendamento_consultas.dto.response.RegisterResponse;
import com.example.agendamento_consultas.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints de autenticação e gestão de sessão.")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Cadastrar usuário",
            description = "Cadastra um novo usuário médico. Requer token JWT com perfil ADMIN.",
            security = @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_SCHEME)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = RegisterResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email já cadastrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para cadastro do usuário.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegisterRequest.class))
            )
            @RequestBody @Valid RegisterRequest request,
            @Parameter(hidden = true)
            HttpServletRequest servletRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(request, servletRequest));
    }

    @Operation(summary = "Realizar login", description = "Autentica usuário e retorna access token + refresh token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciais de acesso.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginRequest.class))
            )
            @RequestBody @Valid LoginRequest request,
            @Parameter(hidden = true)
            HttpServletRequest servletRequest
    ) {
        return ResponseEntity.ok(authService.login(request, servletRequest));
    }

    @Operation(summary = "Renovar token", description = "Gera novo access token e refresh token com um refresh token válido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token renovado com sucesso",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Refresh token inválido ou expirado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Refresh token atual.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RefreshTokenRequest.class))
            )
            @RequestBody @Valid RefreshTokenRequest request,
            @Parameter(hidden = true)
            HttpServletRequest servletRequest
    ) {
        return ResponseEntity.ok(authService.refresh(request, servletRequest));
    }

    @Operation(
            summary = "Encerrar sessão",
            description = "Revoga token de acesso atual e todos os refresh tokens ativos do usuário.",
            security = @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_SCHEME)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Logout realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Parameter(description = "Token JWT no formato 'Bearer {token}'.", required = true, example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @Parameter(hidden = true)
            Authentication authentication,
            @Parameter(hidden = true)
            HttpServletRequest servletRequest
    ) {
        authService.logout(authorizationHeader, authentication.getName(), servletRequest);
        return ResponseEntity.noContent().build();
    }
}
