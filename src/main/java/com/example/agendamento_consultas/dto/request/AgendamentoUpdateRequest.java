package com.example.agendamento_consultas.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoUpdateRequest(
        Long pacienteId,
        LocalDate data,
        LocalTime horario
) {
}
