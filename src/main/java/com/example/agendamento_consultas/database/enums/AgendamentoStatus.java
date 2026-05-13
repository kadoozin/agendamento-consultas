package com.example.agendamento_consultas.database.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status do agendamento.")
public enum AgendamentoStatus {
    AGENDADO,
    CONCLUIDO,
    CANCELADO
}
