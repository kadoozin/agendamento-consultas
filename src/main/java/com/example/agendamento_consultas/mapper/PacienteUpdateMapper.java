package com.example.agendamento_consultas.mapper;

import com.example.agendamento_consultas.database.model.Paciente;
import com.example.agendamento_consultas.dto.request.PacienteUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PacienteUpdateMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contatos", ignore = true)
    void updateEntity(PacienteUpdateRequest request, @MappingTarget Paciente paciente);
}
