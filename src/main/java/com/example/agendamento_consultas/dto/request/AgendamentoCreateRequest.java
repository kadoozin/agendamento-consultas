package com.example.agendamento_consultas.dto.request;

import com.example.agendamento_consultas.database.enums.TipoConsulta;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoCreateRequest(
        @NotNull Long pacienteId,
        @NotNull @JsonFormat(pattern = "dd/MM/yyyy") LocalDate data,
        @NotNull @JsonFormat(pattern = "HH:mm") LocalTime horario,
        @NotNull TipoConsulta tipoConsulta
) {
}
