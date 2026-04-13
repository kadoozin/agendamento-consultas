package com.example.agendamento_consultas.dto.request;

import com.example.agendamento_consultas.database.enums.AgendamentoStatus;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusRequest(
        @NotNull AgendamentoStatus atualizarStatus
        ) {
}
