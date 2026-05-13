package com.example.agendamento_consultas.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ContatoResponse", description = "Representação de contato de paciente.")
public record ContatoResponse(
        @Schema(description = "ID do contato.", example = "15")
        Long id,
        @Schema(description = "Email de contato.", example = "maria@email.com")
        String email,
        @Schema(description = "Número de telefone de contato.", example = "+5511999999999")
        String numero,
        @Schema(description = "Paciente relacionado ao contato.")
        PacienteResumoResponse paciente
) {
}
