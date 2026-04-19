package com.example.agendamento_consultas.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContatoCreateRequest(
        @NotNull Long pacienteId,
        @NotBlank @Email String email,
        @NotBlank String numero
) {
}
