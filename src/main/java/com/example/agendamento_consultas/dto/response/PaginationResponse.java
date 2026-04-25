package com.example.agendamento_consultas.dto.response;

public record PaginationResponse(
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
}