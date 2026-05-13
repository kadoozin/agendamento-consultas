package com.example.agendamento_consultas.dto.request;

import com.example.agendamento_consultas.database.enums.AgendamentoStatus;
import com.example.agendamento_consultas.database.enums.TipoConsulta;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(name = "AgendamentoUpdateRequest", description = "Payload parcial para atualização de agendamento.")
public record AgendamentoUpdateRequest(
        @Schema(description = "Novo paciente vinculado ao agendamento.", example = "2")
        Long pacienteId,
        @Schema(description = "Nova data da consulta no formato dd/MM/yyyy.", example = "21/06/2026", type = "string", format = "date")
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate data,
        @Schema(description = "Novo horário de início no formato HH:mm.", example = "15:00", type = "string")
        @JsonFormat(pattern = "HH:mm") LocalTime horario,
        @Schema(description = "Nova duração em minutos (15 a 480).", example = "45", minimum = "15", maximum = "480")
        @Min(15) @Max(480) Integer duracaoMinutos,
        @Schema(description = "Novo status do agendamento.", example = "AGENDADO")
        AgendamentoStatus status,
        @Schema(description = "Novo tipo da consulta.", example = "ONLINE")
        TipoConsulta tipoConsulta
) {
}
