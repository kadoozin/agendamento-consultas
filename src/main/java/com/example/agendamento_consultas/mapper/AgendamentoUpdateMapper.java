package com.example.agendamento_consultas.mapper;

import com.example.agendamento_consultas.database.model.Agendamento;
import com.example.agendamento_consultas.dto.request.AgendamentoCreateRequest;
import com.example.agendamento_consultas.dto.request.AgendamentoUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AgendamentoUpdateMapper {
    @Mapping(target = "paciente", ignore = true)
    void updateEntity(AgendamentoUpdateRequest request, @MappingTarget Agendamento agendamento);
}
