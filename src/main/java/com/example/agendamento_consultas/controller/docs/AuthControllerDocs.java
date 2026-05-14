package com.example.agendamento_consultas.controller.docs;

import com.example.agendamento_consultas.config.OpenApiConfig;
import com.example.agendamento_consultas.dto.request.LoginRequest;
import com.example.agendamento_consultas.dto.request.RefreshTokenRequest;
import com.example.agendamento_consultas.dto.request.RegisterRequest;
import com.example.agendamento_consultas.dto.response.ApiErrorResponse;
import com.example.agendamento_consultas.dto.response.LoginResponse;
import com.example.agendamento_consultas.dto.response.RegisterResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;

public interface AuthControllerDocs {

    @Operation(
            summary = "Cadastrar usuario",
            description = "Cadastra um novo usuario medico (somente admin).",
            security = @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_SCHEME)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = RegisterResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email ja cadastrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<RegisterResponse> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para cadastro do usuario.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegisterRequest.class))
            )
            RegisterRequest request,
            @Parameter(hidden = true)
            HttpServletRequest servletRequest
    );

    @Operation(
            summary = "Bootstrap do primeiro admin",
            description = "Cria o primeiro usuario admin quando ainda nao existe admin cadastrado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Admin criado com sucesso",
                    content = @Content(schema = @Schema(implementation = RegisterResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bootstrap key invalida ou dados invalidos",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Administrador ja cadastrado ou email ja existente",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<RegisterResponse> bootstrapAdmin(
            @Parameter(description = "Chave de bootstrap do admin.", required = true, example = "minha-chave-segura")
            String bootstrapKey,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para cadastro do primeiro admin.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegisterRequest.class))
            )
            RegisterRequest request,
            @Parameter(hidden = true)
            HttpServletRequest servletRequest
    );

    @Operation(summary = "Realizar login", description = "Autentica usuario e retorna access token + refresh token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais invalidas",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<LoginResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciais de acesso.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginRequest.class))
            )
            LoginRequest request,
            @Parameter(hidden = true)
            HttpServletRequest servletRequest
    );

    @Operation(summary = "Renovar token", description = "Gera novo access token e refresh token com um refresh token valido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token renovado com sucesso",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Refresh token invalido ou expirado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<LoginResponse> refresh(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Refresh token atual.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RefreshTokenRequest.class))
            )
            RefreshTokenRequest request,
            @Parameter(hidden = true)
            HttpServletRequest servletRequest
    );

    @Operation(
            summary = "Encerrar sessao",
            description = "Revoga token de acesso atual e todos os refresh tokens ativos do usuario.",
            security = @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_SCHEME)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Logout realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Nao autenticado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<Void> logout(
            @Parameter(description = "Token JWT no formato 'Bearer {token}'.", required = true, example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            String authorizationHeader,
            @Parameter(hidden = true)
            Authentication authentication,
            @Parameter(hidden = true)
            HttpServletRequest servletRequest
    );
}
