package com.example.agendamento_consultas.mapper;

import com.example.agendamento_consultas.database.model.Agendamento;
import com.example.agendamento_consultas.dto.request.AgendamentoCreateRequest;
import com.example.agendamento_consultas.dto.response.AgendamentoResponse;
import org.mapstruct.*;

import java.util.List;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface AgendamentoMapper {
    AgendamentoResponse toResponse(Agendamento agendamento);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "paciente", ignore = true)
    @Mapping(target = "status", ignore = true)
    Agendamento toEntity(AgendamentoCreateRequest request);

    List<AgendamentoResponse> toResponseList(List<Agendamento> agendamentos);

    default Page<AgendamentoResponse> toResponsePage(Page<Agendamento> agendamentos) {
        return agendamentos.map(this::toResponse);
    }
}
