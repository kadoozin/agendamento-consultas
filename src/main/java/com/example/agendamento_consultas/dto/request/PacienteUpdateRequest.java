package com.example.agendamento_consultas.dto.request;

import java.time.LocalDate;

public record PacienteUpdateRequest(
        String nomeCompleto,
        LocalDate dataNascimento,
        String documentoIdentificacao

) {
}
