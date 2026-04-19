package com.example.agendamento_consultas.dto.request;

import com.example.agendamento_consultas.database.enums.AgendamentoStatus;
import com.example.agendamento_consultas.database.enums.TipoConsulta;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoUpdateRequest(
        Long pacienteId,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate data,
        @JsonFormat(pattern = "HH:mm") LocalTime horario,
        AgendamentoStatus status,
        TipoConsulta tipoConsulta
) {
}
