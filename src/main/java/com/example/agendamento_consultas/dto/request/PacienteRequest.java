package com.example.agendamento_consultas.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;

public record PacienteRequest(
        @NotBlank String nomeCompleto,
        @NotNull @JsonFormat(pattern = "dd/MM/yyyy") LocalDate dataNascimento,
        @NotBlank String documentoIdentificacao,
        Set<ContatoRequest> contatos
) {
}
