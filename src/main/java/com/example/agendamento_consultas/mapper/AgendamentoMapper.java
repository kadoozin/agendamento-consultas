package com.example.agendamento_consultas.mapper;

import com.example.agendamento_consultas.database.model.Agendamento;
import com.example.agendamento_consultas.dto.request.AgendamentoRequest;
import com.example.agendamento_consultas.dto.response.AgendamentoResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AgendamentoMapper {
    AgendamentoResponse toResponse(Agendamento agendamento);

    @Mapping(target = "paciente", ignore = true)
    Agendamento toEntity(AgendamentoRequest request);

    List<AgendamentoResponse> toResponseList(List<Agendamento> agendamentos);
}
