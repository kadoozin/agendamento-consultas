package com.example.agendamento_consultas.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "PacientePageResponse", description = "Resposta paginada de pacientes.")
public record PacientePageResponse(
        @Schema(description = "Lista de pacientes da página.")
        List<PacienteResponse> data,
        @Schema(description = "Metadados de paginação.")
        PaginationResponse pagination
) {
}
