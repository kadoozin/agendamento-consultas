package com.example.agendamento_consultas.mapper;

import com.example.agendamento_consultas.database.model.Contato;
import com.example.agendamento_consultas.dto.request.ContatoCreateRequest;
import com.example.agendamento_consultas.dto.request.ContatoUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ContatoUpdateMapper {
    void updateEntity(ContatoUpdateRequest request, @MappingTarget Contato contato);
}
