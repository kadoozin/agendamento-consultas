package com.example.agendamento_consultas.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

@Schema(name = "ContatoUpdateRequest", description = "Payload para atualização de contato.")
public record ContatoUpdateRequest(
        @Schema(description = "Novo ID do paciente associado ao contato.", example = "2")
        Long pacienteId,
        @Schema(description = "Novo email do contato.", example = "novo@email.com")
        @Email String email,
        @Schema(description = "Novo número de telefone.", example = "+5511988887777")
        String numero
) {
}
