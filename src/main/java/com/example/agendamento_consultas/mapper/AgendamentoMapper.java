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

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "paciente", ignore = true)
    void updateEntity(AgendamentoRequest request, @MappingTarget Agendamento agendamento);

    List<AgendamentoResponse> toResponseList(List<Agendamento> agendamentos);
}
