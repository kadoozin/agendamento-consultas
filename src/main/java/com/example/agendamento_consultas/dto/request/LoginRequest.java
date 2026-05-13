package com.example.agendamento_consultas.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "LoginRequest", description = "Credenciais para autenticação.")
public record LoginRequest(
        @Schema(description = "Email do usuário.", example = "medico@clinica.com")
        @NotBlank String email,
        @Schema(description = "Senha do usuário.", example = "Senha@123")
        @NotBlank String senha
) {
}
