package com.example.agendamento_consultas.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "PacienteResponse", description = "Representação de paciente.")
public record PacienteResponse(
        @Schema(description = "ID do paciente.", example = "1")
        Long id,
        @Schema(description = "Nome completo.", example = "Maria da Silva")
        String nomeCompleto,
        @Schema(description = "Data de nascimento no formato yyyy-MM-dd.", example = "1990-07-21", type = "string", format = "date")
        LocalDate dataNascimento,
        @Schema(description = "Documento de identificação único.", example = "12345678900")
        String documentoIdentificacao
) {
}
