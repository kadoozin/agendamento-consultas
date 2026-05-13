package com.example.agendamento_consultas.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(name = "ReagendamentoRequest", description = "Payload para reagendamento de consulta.")
public record ReagendamentoRequest(
        @Schema(description = "Nova data da consulta no formato dd/MM/yyyy.", example = "25/06/2026", type = "string", format = "date")
        @NotNull @JsonFormat(pattern = "dd/MM/yyyy") LocalDate data,
        @Schema(description = "Novo horário de início no formato HH:mm.", example = "10:00", type = "string")
        @NotNull @JsonFormat(pattern = "HH:mm") LocalTime horario,
        @Schema(description = "Nova duração da consulta em minutos (15 a 480).", example = "30", minimum = "15", maximum = "480")
        @NotNull @Min(15) @Max(480) Integer duracaoMinutos
) {
}
