package com.example.agendamento_consultas.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PacienteResumoResponse", description = "Representação resumida de paciente.")
public record PacienteResumoResponse(
        @Schema(description = "ID do paciente.", example = "1")
        Long id,
        @Schema(description = "Nome completo do paciente.", example = "Maria da Silva")
        String nomeCompleto
) {
}
