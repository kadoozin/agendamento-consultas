package com.example.agendamento_consultas.controller.docs;

import com.example.agendamento_consultas.database.enums.TipoConsulta;
import com.example.agendamento_consultas.dto.request.AgendamentoCreateRequest;
import com.example.agendamento_consultas.dto.request.AgendamentoUpdateRequest;
import com.example.agendamento_consultas.dto.request.ReagendamentoRequest;
import com.example.agendamento_consultas.dto.response.AgendamentoPageResponse;
import com.example.agendamento_consultas.dto.response.AgendamentoResponse;
import com.example.agendamento_consultas.dto.response.ApiErrorResponse;
import com.example.agendamento_consultas.dto.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface AgendamentoControllerDocs {

    @Operation(summary = "Criar agendamento", description = "Cria um novo agendamento e dispara notificacao de confirmacao.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Agendamento criado",
                    content = @Content(schema = @Schema(implementation = AgendamentoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou regra de negocio violada",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Paciente nao encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<AgendamentoResponse> criar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para criacao de agendamento.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AgendamentoCreateRequest.class))
            )
            AgendamentoCreateRequest request
    );

    @Operation(summary = "Buscar agendamento por ID", description = "Retorna os dados de um agendamento especifico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento encontrado",
                    content = @Content(schema = @Schema(implementation = AgendamentoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Agendamento nao encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<AgendamentoResponse> buscarPorId(
            @Parameter(description = "ID do agendamento.", required = true, example = "200")
            Long id
    );

    @Operation(summary = "Listar agendamentos", description = "Lista agendamentos com paginacao e filtro opcional por tipo de consulta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = AgendamentoPageResponse.class)))
    })
    ResponseEntity<PageResponse<AgendamentoResponse>> listar(
            @Parameter(description = "Filtro por tipo de consulta.", example = "PRESENCIAL")
            TipoConsulta tipoConsulta,
            @ParameterObject Pageable pageable
    );

    @Operation(summary = "Atualizar agendamento", description = "Atualiza parcialmente os dados do agendamento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento atualizado",
                    content = @Content(schema = @Schema(implementation = AgendamentoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou regra de negocio violada",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Agendamento ou paciente nao encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<AgendamentoResponse> atualizar(
            @Parameter(description = "ID do agendamento.", required = true, example = "200")
            Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Campos para atualizacao parcial de agendamento.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AgendamentoUpdateRequest.class))
            )
            AgendamentoUpdateRequest request
    );

    @Operation(summary = "Reagendar consulta", description = "Altera data, horario e duracao de um agendamento existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento reagendado",
                    content = @Content(schema = @Schema(implementation = AgendamentoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou regra de negocio violada",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Agendamento nao encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<AgendamentoResponse> reagendar(
            @Parameter(description = "ID do agendamento.", required = true, example = "200")
            Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novos dados para reagendamento.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ReagendamentoRequest.class))
            )
            ReagendamentoRequest request
    );

    @Operation(summary = "Cancelar agendamento", description = "Marca um agendamento como cancelado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento cancelado",
                    content = @Content(schema = @Schema(implementation = AgendamentoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Regra de negocio violada",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Agendamento nao encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<AgendamentoResponse> cancelar(
            @Parameter(description = "ID do agendamento.", required = true, example = "200")
            Long id
    );

    @Operation(summary = "Excluir agendamento", description = "Remove um agendamento do sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Agendamento removido"),
            @ApiResponse(responseCode = "404", description = "Agendamento nao encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<Void> deletar(
            @Parameter(description = "ID do agendamento.", required = true, example = "200")
            Long id
    );
}
