package com.example.agendamento_consultas.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ViaCepResponse", description = "Resposta retornada pela consulta de CEP.")
public record ViaCepResponse(
        @Schema(description = "CEP consultado.", example = "01001000")
        String cep,
        @Schema(description = "Logradouro.", example = "Praça da Sé")
        String logradouro,
        @Schema(description = "Complemento.", example = "lado ímpar")
        String complemento,
        @Schema(description = "Bairro.", example = "Sé")
        String bairro,
        @Schema(description = "Cidade.", example = "São Paulo")
        String localidade,
        @Schema(description = "UF.", example = "SP")
        String uf,
        @Schema(description = "Indica erro de CEP não encontrado.", example = "false")
        Boolean erro
) {
}
