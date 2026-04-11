package com.example.agendamento_consultas.mapper;

import com.example.agendamento_consultas.database.model.Paciente;
import com.example.agendamento_consultas.dto.request.PacienteRequest;
import com.example.agendamento_consultas.dto.response.PacienteResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PacienteMapper {
    PacienteResponse toResponse(Paciente paciente);

    Paciente toEntity(PacienteRequest request);

    void updateEntity(PacienteRequest request, @MappingTarget Paciente paciente);

    List<PacienteResponse> toResponseList(List<Paciente> pacientes);
}
