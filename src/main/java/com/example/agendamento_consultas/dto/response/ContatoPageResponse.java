package com.example.agendamento_consultas.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "ContatoPageResponse", description = "Resposta paginada de contatos.")
public record ContatoPageResponse(
        @Schema(description = "Lista de contatos da página.")
        List<ContatoResponse> data,
        @Schema(description = "Metadados de paginação.")
        PaginationResponse pagination
) {
}
