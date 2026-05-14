package com.example.agendamento_consultas.controller.docs;

import com.example.agendamento_consultas.dto.response.ApiErrorResponse;
import com.example.agendamento_consultas.dto.response.ViaCepResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface ViaCepControllerDocs {

    @Operation(summary = "Buscar endereco por CEP", description = "Consulta endereco no ViaCEP a partir de um CEP valido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereco encontrado",
                    content = @Content(schema = @Schema(implementation = ViaCepResponse.class))),
            @ApiResponse(responseCode = "400", description = "CEP invalido",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "CEP nao encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "Servico externo indisponivel",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<ViaCepResponse> buscarPorCep(
            @Parameter(description = "CEP com 8 digitos, com ou sem mascara.", required = true, example = "01001000")
            String cep
    );
}
