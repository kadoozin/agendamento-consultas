package com.example.agendamento_consultas.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(name = "PacienteCreateRequest", description = "Payload para criação de paciente.")
public record PacienteCreateRequest(
        @Schema(description = "Nome completo do paciente.", example = "Maria da Silva")
        @NotBlank String nomeCompleto,
        @Schema(description = "Data de nascimento no formato dd/MM/yyyy.", example = "21/07/1990", type = "string", format = "date")
        @NotNull @JsonFormat(pattern = "dd/MM/yyyy") LocalDate dataNascimento,
        @Schema(description = "Documento único de identificação.", example = "12345678900")
        @NotBlank String documentoIdentificacao
) {
}
