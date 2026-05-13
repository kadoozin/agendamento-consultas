package com.example.agendamento_consultas.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(name = "PacienteUpdateRequest", description = "Payload parcial para atualização de paciente.")
public record PacienteUpdateRequest(
        @Schema(description = "Nome completo do paciente.", example = "Maria da Silva")
        String nomeCompleto,
        @Schema(description = "Data de nascimento no formato dd/MM/yyyy", example = "02/02/2002", type = "string", format = "date")
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate dataNascimento,
        @Schema(description = "Documento único de identificação.", example = "12345678900")
        String documentoIdentificacao

) {
}
