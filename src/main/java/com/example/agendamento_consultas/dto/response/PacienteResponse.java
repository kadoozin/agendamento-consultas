package com.example.agendamento_consultas.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PacienteResponse(
        Long id,
        String nomeCompleto,
        LocalDate dataNascimento,
        String documentoIdentificacao,
        Set<ContatoResponse> contatos
) {
}
