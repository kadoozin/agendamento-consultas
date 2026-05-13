package com.example.agendamento_consultas.dto.response;

import com.example.agendamento_consultas.database.enums.AgendamentoStatus;
import com.example.agendamento_consultas.database.enums.TipoConsulta;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "AgendamentoResponse", description = "Representação de agendamento de consulta.")
public record AgendamentoResponse(
        @Schema(description = "ID do agendamento.", example = "200")
        Long id,
        @Schema(description = "Paciente associado ao agendamento.")
        PacienteResponse paciente,
        @Schema(description = "Data da consulta no formato dd/MM/yyyy.", example = "20/06/2026", type = "string", format = "date")
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate data,
        @Schema(description = "Horário de início no formato HH:mm.", example = "14:30", type = "string")
        @JsonFormat(pattern = "HH:mm") LocalTime horario,
        @Schema(description = "Horário de término no formato HH:mm.", example = "15:30", type = "string")
        @JsonFormat(pattern = "HH:mm") LocalTime horarioFim,
        @Schema(description = "Duração em minutos.", example = "60")
        Integer duracaoMinutos,
        @Schema(description = "Status atual do agendamento.", example = "AGENDADO")
        AgendamentoStatus status,
        @Schema(description = "Tipo da consulta.", example = "PRESENCIAL")
        TipoConsulta tipoConsulta
) {
}
