package com.example.agendamento_consultas.mapper;

import com.example.agendamento_consultas.database.model.Contato;
import com.example.agendamento_consultas.dto.request.ContatoCreateRequest;
import com.example.agendamento_consultas.dto.response.ContatoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContatoMapper {
    ContatoResponse toResponse(Contato contato);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "paciente", ignore = true)
    Contato toEntity(ContatoCreateRequest request);

    List<ContatoResponse> toResponseList(List<Contato> contatos);
}
