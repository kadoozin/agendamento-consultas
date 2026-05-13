package com.example.agendamento_consultas.dto.request;

import com.example.agendamento_consultas.database.enums.TipoConsulta;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(name = "AgendamentoCreateRequest", description = "Payload para criação de agendamento.")
public record AgendamentoCreateRequest(
        @Schema(description = "ID do paciente que será atendido.", example = "1")
        @NotNull Long pacienteId,
        @Schema(description = "Data da consulta no formato dd/MM/yyyy.", example = "20/06/2026", type = "string", format = "date")
        @NotNull @JsonFormat(pattern = "dd/MM/yyyy") LocalDate data,
        @Schema(description = "Horário de início no formato HH:mm.", example = "14:30", type = "string")
        @NotNull @JsonFormat(pattern = "HH:mm") LocalTime horario,
        @Schema(description = "Duração da consulta em minutos (15 a 480).", example = "60", minimum = "15", maximum = "480")
        @NotNull @Min(15) @Max(480) Integer duracaoMinutos,
        @Schema(description = "Tipo da consulta.", example = "PRESENCIAL")
        @NotNull TipoConsulta tipoConsulta
) {
}
