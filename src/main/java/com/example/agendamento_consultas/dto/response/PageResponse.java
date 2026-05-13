package com.example.agendamento_consultas.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(name = "PageResponse", description = "Envelope padrão para respostas paginadas.")
public record PageResponse<T>(
        @Schema(description = "Lista de itens retornados para a página solicitada.")
        List<T> data,
        @Schema(description = "Metadados de paginação.")
        PaginationResponse pagination
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                new PaginationResponse(
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages(),
                        page.isFirst(),
                        page.isLast()
                )
        );
    }
}
