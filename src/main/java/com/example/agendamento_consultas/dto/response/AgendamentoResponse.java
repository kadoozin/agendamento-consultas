package com.example.agendamento_consultas.dto.response;

import com.example.agendamento_consultas.database.enums.AgendamentoStatus;
import com.example.agendamento_consultas.database.enums.TipoConsulta;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AgendamentoResponse(
        Long id,
        PacienteResponse paciente,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate data,
        @JsonFormat(pattern = "HH:mm") LocalTime horario,
        @JsonFormat(pattern = "HH:mm") LocalTime horarioFim,
        Integer duracaoMinutos,
        AgendamentoStatus status,
        TipoConsulta tipoConsulta
) {
}
