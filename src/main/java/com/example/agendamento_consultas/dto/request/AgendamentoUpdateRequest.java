package com.example.agendamento_consultas.dto.request;

import com.example.agendamento_consultas.database.enums.AgendamentoStatus;
import com.example.agendamento_consultas.database.enums.TipoConsulta;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoUpdateRequest(
        Long pacienteId,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate data,
        @JsonFormat(pattern = "HH:mm") LocalTime horario,
        @Min(15) @Max(480) Integer duracaoMinutos,
        AgendamentoStatus status,
        TipoConsulta tipoConsulta
) {
}
