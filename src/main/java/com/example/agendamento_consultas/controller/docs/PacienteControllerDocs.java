package com.example.agendamento_consultas.controller.docs;

import com.example.agendamento_consultas.dto.request.PacienteCreateRequest;
import com.example.agendamento_consultas.dto.request.PacienteUpdateRequest;
import com.example.agendamento_consultas.dto.response.ApiErrorResponse;
import com.example.agendamento_consultas.dto.response.PageResponse;
import com.example.agendamento_consultas.dto.response.PacientePageResponse;
import com.example.agendamento_consultas.dto.response.PacienteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface PacienteControllerDocs {

    @Operation(summary = "Criar paciente", description = "Cadastra um novo paciente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente criado",
                    content = @Content(schema = @Schema(implementation = PacienteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Documento ja cadastrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<PacienteResponse> criar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para criacao de paciente.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PacienteCreateRequest.class))
            )
            PacienteCreateRequest request
    );

    @Operation(summary = "Buscar paciente por ID", description = "Retorna os dados detalhados de um paciente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado",
                    content = @Content(schema = @Schema(implementation = PacienteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Paciente nao encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<PacienteResponse> buscarPorId(
            @Parameter(description = "ID do paciente.", required = true, example = "1")
            Long id
    );

    @Operation(summary = "Buscar pacientes com filtros", description = "Busca por nome e/ou documento com retorno paginado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso",
                    content = @Content(schema = @Schema(implementation = PacientePageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Paciente nao encontrado para o filtro informado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<PageResponse<PacienteResponse>> buscar(
            @Parameter(description = "Filtro por nome completo (parcial, case-insensitive).", example = "Maria")
            String nomeCompleto,
            @Parameter(description = "Filtro por documento de identificacao exato.", example = "12345678900")
            String documentoIdentificacao,
            @ParameterObject Pageable pageable
    );

    @Operation(summary = "Listar pacientes", description = "Lista pacientes com paginacao.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = PacientePageResponse.class)))
    })
    ResponseEntity<PageResponse<PacienteResponse>> listar(@ParameterObject Pageable pageable);

    @Operation(summary = "Atualizar paciente", description = "Atualiza parcialmente os dados de um paciente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente atualizado",
                    content = @Content(schema = @Schema(implementation = PacienteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Paciente nao encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Documento ja cadastrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<PacienteResponse> atualizar(
            @Parameter(description = "ID do paciente.", required = true, example = "1")
            Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Campos para atualizacao parcial.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PacienteUpdateRequest.class))
            )
            PacienteUpdateRequest request
    );

    @Operation(summary = "Excluir paciente", description = "Remove o paciente pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paciente removido"),
            @ApiResponse(responseCode = "404", description = "Paciente nao encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<Void> deletar(
            @Parameter(description = "ID do paciente.", required = true, example = "1")
            Long id
    );
}
