package com.example.agendamento_consultas.mapper;

import com.example.agendamento_consultas.database.model.Contato;
import com.example.agendamento_consultas.dto.request.ContatoRequest;
import com.example.agendamento_consultas.dto.response.ContatoResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ContatoMapper {
    ContatoResponse toResponse(Contato contato);

    Contato toEntity(ContatoRequest request);

    void updateEntity(ContatoRequest request, @MappingTarget Contato contato);

    List<ContatoResponse> toResponseList(List<Contato> contatos);
}
