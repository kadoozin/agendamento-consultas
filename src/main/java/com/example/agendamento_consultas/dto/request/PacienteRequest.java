package com.example.agendamento_consultas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;

public record PacienteRequest(
        @NotBlank String nomeCompleto,
        @NotNull LocalDate dataNascimento,
        @NotBlank String documentoIdentificacao,
        Set<ContatoRequest> contatos
) {
}
