package com.example.agendamento_consultas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContatoRequest(
        @NotNull Long pacienteId,
        @NotBlank String email,
        @NotBlank String numero
) {
}
