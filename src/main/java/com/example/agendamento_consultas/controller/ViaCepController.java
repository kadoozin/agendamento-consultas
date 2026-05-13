package com.example.agendamento_consultas.controller;

import com.example.agendamento_consultas.config.OpenApiConfig;
import com.example.agendamento_consultas.dto.response.ApiErrorResponse;
import com.example.agendamento_consultas.dto.response.ViaCepResponse;
import com.example.agendamento_consultas.integrations.ViaCepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/enderecos")
@RequiredArgsConstructor
@Tag(name = "Endereços", description = "Consulta de endereços via integração com ViaCEP.")
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_SCHEME)
public class ViaCepController {

    private final ViaCepService viaCepService;

    @Operation(summary = "Buscar endereço por CEP", description = "Consulta endereço no ViaCEP a partir de um CEP válido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço encontrado",
                    content = @Content(schema = @Schema(implementation = ViaCepResponse.class))),
            @ApiResponse(responseCode = "400", description = "CEP inválido",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "CEP não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "Serviço externo indisponível",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/cep/{cep}")
    public ResponseEntity<ViaCepResponse> buscarPorCep(
            @Parameter(description = "CEP com 8 dígitos, com ou sem máscara.", required = true, example = "01001000")
            @PathVariable String cep){
        return ResponseEntity.ok(viaCepService.buscarPorCep(cep));
    }
}
