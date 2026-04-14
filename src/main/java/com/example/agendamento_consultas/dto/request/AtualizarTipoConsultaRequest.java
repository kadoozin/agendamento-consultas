package com.example.agendamento_consultas.dto.request;

import com.example.agendamento_consultas.database.enums.TipoConsulta;
import jakarta.validation.constraints.NotNull;

public record AtualizarTipoConsultaRequest(
        @NotNull TipoConsulta atualizarTipoConsulta
        ) {
}
