package com.example.agendamento_consultas.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ContatoRequest(
        @NotBlank String email,
        @NotBlank String numero
) {
}
