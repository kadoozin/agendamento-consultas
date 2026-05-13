package com.example.agendamento_consultas.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RegisterResponse", description = "Dados do usuário criado.")
public record RegisterResponse(
        @Schema(description = "Identificador do usuário.", example = "10")
        Long id,
        @Schema(description = "Email do usuário cadastrado.", example = "medico@clinica.com")
        String email
) {
}
