package com.example.agendamento_consultas.dto.request;

import jakarta.validation.constraints.Email;

public record ContatoUpdateRequest(
        Long pacienteId,
       @Email String email,
        String numero
) {
}
