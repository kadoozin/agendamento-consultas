package com.example.agendamento_consultas.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "AgendamentoPageResponse", description = "Resposta paginada de agendamentos.")
public record AgendamentoPageResponse(
        @Schema(description = "Lista de agendamentos da página.")
        List<AgendamentoResponse> data,
        @Schema(description = "Metadados de paginação.")
        PaginationResponse pagination
) {
}
