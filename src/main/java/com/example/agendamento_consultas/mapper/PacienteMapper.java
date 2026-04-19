package com.example.agendamento_consultas.mapper;

import com.example.agendamento_consultas.database.model.Paciente;
import com.example.agendamento_consultas.dto.request.PacienteCreateRequest;
import com.example.agendamento_consultas.dto.response.PacienteResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PacienteMapper {
    PacienteResponse toResponse(Paciente paciente);

    Paciente toEntity(PacienteCreateRequest request);

    List<PacienteResponse> toResponseList(List<Paciente> pacientes);
}
