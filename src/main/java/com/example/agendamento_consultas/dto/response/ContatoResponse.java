package com.example.agendamento_consultas.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ContatoResponse(
        Long id,
        String email,
        String numero,
        PacienteResumoResponse paciente
) {
}
