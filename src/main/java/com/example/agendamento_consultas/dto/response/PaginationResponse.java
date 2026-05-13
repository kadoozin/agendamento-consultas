package com.example.agendamento_consultas.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PaginationResponse", description = "Metadados de paginação da resposta.")
public record PaginationResponse(
        @Schema(description = "Número da página atual (base 0).", example = "0")
        int page,
        @Schema(description = "Quantidade de itens por página.", example = "20")
        int size,
        @Schema(description = "Total de elementos da consulta.", example = "135")
        long totalElements,
        @Schema(description = "Total de páginas.", example = "7")
        int totalPages,
        @Schema(description = "Indica se é a primeira página.", example = "true")
        boolean first,
        @Schema(description = "Indica se é a última página.", example = "false")
        boolean last
) {
}
