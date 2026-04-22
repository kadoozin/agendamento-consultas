package com.example.agendamento_consultas.mapper;

import com.example.agendamento_consultas.database.model.Paciente;
import com.example.agendamento_consultas.dto.request.PacienteCreateRequest;
import com.example.agendamento_consultas.dto.response.PacienteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface PacienteMapper {
    PacienteResponse toResponse(Paciente paciente);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contatos", ignore = true)
    Paciente toEntity(PacienteCreateRequest request);

    List<PacienteResponse> toResponseList(List<Paciente> pacientes);

    default Page<PacienteResponse> toResponsePage(Page<Paciente> pacientes) {
        return pacientes.map(this::toResponse);
    }
}
