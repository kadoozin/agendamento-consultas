package com.example.agendamento_consultas.dto.response;

import java.time.LocalDate;
import java.util.Set;

public record PacienteResponse(
        Long id,
        String nomeCompleto,
        LocalDate dataNascimento,
        String documentoIdentificacao,
        Set<ContatoResponse> contato
) {
}
