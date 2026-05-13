package com.example.agendamento_consultas.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "ContatoCreateRequest", description = "Payload para criação de contato de paciente.")
public record ContatoCreateRequest(
        @Schema(description = "ID do paciente dono do contato.", example = "1")
        @NotNull Long pacienteId,
        @Schema(description = "Email de contato único.", example = "maria@email.com")
        @NotBlank @Email String email,
        @Schema(description = "Número de telefone para notificações.", example = "+5511999999999")
        @NotBlank String numero
) {
}
