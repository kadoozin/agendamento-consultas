package com.example.agendamento_consultas.controller;

import com.example.agendamento_consultas.config.OpenApiConfig;
import com.example.agendamento_consultas.database.enums.TipoConsulta;
import com.example.agendamento_consultas.dto.request.AgendamentoCreateRequest;
import com.example.agendamento_consultas.dto.request.AgendamentoUpdateRequest;
import com.example.agendamento_consultas.dto.request.ReagendamentoRequest;
import com.example.agendamento_consultas.dto.response.AgendamentoPageResponse;
import com.example.agendamento_consultas.dto.response.ApiErrorResponse;
import com.example.agendamento_consultas.dto.response.AgendamentoResponse;
import com.example.agendamento_consultas.dto.response.PageResponse;
import com.example.agendamento_consultas.service.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/agendamentos")
@RequiredArgsConstructor
@Tag(name = "Agendamentos", description = "Gestão de agendamentos de consultas.")
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_SCHEME)
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @Operation(summary = "Criar agendamento", description = "Cria um novo agendamento e dispara notificação de confirmação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Agendamento criado",
                    content = @Content(schema = @Schema(implementation = AgendamentoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<AgendamentoResponse> criar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para criação de agendamento.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AgendamentoCreateRequest.class))
            )
            @RequestBody @Valid AgendamentoCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.criar(request));
    }

    @Operation(summary = "Buscar agendamento por ID", description = "Retorna os dados de um agendamento específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento encontrado",
                    content = @Content(schema = @Schema(implementation = AgendamentoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> buscarPorId(
            @Parameter(description = "ID do agendamento.", required = true, example = "200")
            @PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.buscarPorId(id));
    }

    @Operation(summary = "Listar agendamentos", description = "Lista agendamentos com paginação e filtro opcional por tipo de consulta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = AgendamentoPageResponse.class)))
    })
    @GetMapping
    public ResponseEntity<PageResponse<AgendamentoResponse>> listar(
            @Parameter(description = "Filtro por tipo de consulta.", example = "PRESENCIAL")
            @RequestParam(required = false) TipoConsulta tipoConsulta,
            @ParameterObject
            @PageableDefault(size = 20, sort = "data", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<AgendamentoResponse> page = agendamentoService.listar(tipoConsulta, pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @Operation(summary = "Atualizar agendamento", description = "Atualiza parcialmente os dados do agendamento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento atualizado",
                    content = @Content(schema = @Schema(implementation = AgendamentoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Agendamento ou paciente não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> atualizar(
            @Parameter(description = "ID do agendamento.", required = true, example = "200")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Campos para atualização parcial de agendamento.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AgendamentoUpdateRequest.class))
            )
            @RequestBody @Valid AgendamentoUpdateRequest request) {

        return ResponseEntity.ok(agendamentoService.atualizar(id, request));
    }

    @Operation(summary = "Reagendar consulta", description = "Altera data, horário e duração de um agendamento existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento reagendado",
                    content = @Content(schema = @Schema(implementation = AgendamentoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PatchMapping("/{id}/reagendamento")
    public ResponseEntity<AgendamentoResponse> reagendar(
            @Parameter(description = "ID do agendamento.", required = true, example = "200")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novos dados para reagendamento.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ReagendamentoRequest.class))
            )
            @RequestBody @Valid ReagendamentoRequest request) {

        return ResponseEntity.ok(agendamentoService.reagendar(id, request));
    }

    @Operation(summary = "Cancelar agendamento", description = "Marca um agendamento como cancelado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento cancelado",
                    content = @Content(schema = @Schema(implementation = AgendamentoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Regra de negócio violada",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PatchMapping("/{id}/cancelamento")
    public ResponseEntity<AgendamentoResponse> cancelar(
            @Parameter(description = "ID do agendamento.", required = true, example = "200")
            @PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.cancelar(id));
    }

    @Operation(summary = "Excluir agendamento", description = "Remove um agendamento do sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Agendamento removido"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do agendamento.", required = true, example = "200")
            @PathVariable Long id) {
        agendamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
