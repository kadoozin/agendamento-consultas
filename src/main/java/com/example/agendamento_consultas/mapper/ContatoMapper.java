package com.example.agendamento_consultas.mapper;

import com.example.agendamento_consultas.database.model.Contato;
import com.example.agendamento_consultas.dto.request.ContatoRequest;
import com.example.agendamento_consultas.dto.response.ContatoResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContatoMapper {
    ContatoResponse toResponse(Contato contato);

    Contato toEntity(ContatoRequest request);

    List<ContatoResponse> toResponseList(List<Contato> contatos);
}
