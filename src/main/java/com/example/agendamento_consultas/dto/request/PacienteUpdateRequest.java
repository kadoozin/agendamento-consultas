package com.example.agendamento_consultas.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(name = "PacienteUpdateRequest", description = "Payload parcial para atualização de paciente.")
public record PacienteUpdateRequest(
        @Schema(description = "Nome completo do paciente.", example = "Maria da Silva")
        String nomeCompleto,
        @Schema(description = "Data de nascimento no formato yyyy-MM-dd.", example = "1990-07-21", type = "string", format = "date")
        LocalDate dataNascimento,
        @Schema(description = "Documento único de identificação.", example = "12345678900")
        String documentoIdentificacao

) {
}
