package com.example.agendamento_consultas.dto.response;

import com.example.agendamento_consultas.database.enums.AgendamentoStatus;
import com.example.agendamento_consultas.database.enums.TipoConsulta;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoResponse(
        Long id,
        PacienteResponse paciente,
        LocalDate data,
        LocalTime hora,
        AgendamentoStatus status,
        TipoConsulta tipoConsulta
) {
}
