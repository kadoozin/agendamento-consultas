package com.example.agendamento_consultas.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "RegisterRequest", description = "Payload para cadastro de novo usuário médico.")
public record RegisterRequest(
        @Schema(description = "Email único do usuário.", example = "medico@clinica.com")
        @NotBlank @Email String email,
        @Schema(description = "Senha em texto puro no momento do cadastro.", example = "Senha@123")
        @NotBlank String senha
) {
}
