package com.example.agendamento_consultas.dto.request;

import com.example.agendamento_consultas.database.enums.TipoConsulta;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoRequest(
        @NotNull Long pacienteId,
        @NotNull LocalDate data,
        @NotNull LocalTime hora,
        @NotNull TipoConsulta tipoConsulta
) {
}
