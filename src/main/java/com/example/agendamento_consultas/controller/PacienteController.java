package com.example.agendamento_consultas.controller;

import com.example.agendamento_consultas.config.OpenApiConfig;
import com.example.agendamento_consultas.dto.request.PacienteCreateRequest;
import com.example.agendamento_consultas.dto.request.PacienteUpdateRequest;
import com.example.agendamento_consultas.dto.response.ApiErrorResponse;
import com.example.agendamento_consultas.dto.response.PacientePageResponse;
import com.example.agendamento_consultas.dto.response.PacienteResponse;
import com.example.agendamento_consultas.dto.response.PageResponse;
import com.example.agendamento_consultas.service.PacienteService;
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
@RequestMapping("/v1/pacientes")
@RequiredArgsConstructor
@Tag(name = "Pacientes", description = "Gestão de cadastro e consulta de pacientes.")
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_SCHEME)
public class PacienteController {
    private final PacienteService pacienteService;

    @Operation(summary = "Criar paciente", description = "Cadastra um novo paciente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente criado",
                    content = @Content(schema = @Schema(implementation = PacienteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Documento já cadastrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<PacienteResponse> criar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para criação de paciente.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PacienteCreateRequest.class))
            )
            @RequestBody @Valid PacienteCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.criar(request));
    }

    @Operation(summary = "Buscar paciente por ID", description = "Retorna os dados detalhados de um paciente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado",
                    content = @Content(schema = @Schema(implementation = PacienteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponse> buscarPorId(
            @Parameter(description = "ID do paciente.", required = true, example = "1")
            @PathVariable Long id){
        return ResponseEntity.ok(pacienteService.buscarPorId(id));
    }

    @Operation(summary = "Buscar pacientes com filtros", description = "Busca por nome e/ou documento com retorno paginado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso",
                    content = @Content(schema = @Schema(implementation = PacientePageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado para o filtro informado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/buscar")
    public ResponseEntity<PageResponse<PacienteResponse>> buscar(
            @Parameter(description = "Filtro por nome completo (parcial, case-insensitive).", example = "Maria")
            @RequestParam(required = false) String nomeCompleto,
            @Parameter(description = "Filtro por documento de identificação exato.", example = "12345678900")
            @RequestParam(required = false) String documentoIdentificacao,
            @ParameterObject
            @PageableDefault(size = 20, sort = "nomeCompleto", direction = Sort.Direction.ASC) Pageable pageable){

        Page<PacienteResponse> page = pacienteService.buscar(nomeCompleto, documentoIdentificacao, pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @Operation(summary = "Listar pacientes", description = "Lista pacientes com paginação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = PacientePageResponse.class)))
    })
    @GetMapping
    public ResponseEntity<PageResponse<PacienteResponse>> listar(
            @ParameterObject
            @PageableDefault(size = 20, sort = "nomeCompleto") Pageable pageable){

        Page<PacienteResponse> page = pacienteService.listar(pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @Operation(summary = "Atualizar paciente", description = "Atualiza parcialmente os dados de um paciente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente atualizado",
                    content = @Content(schema = @Schema(implementation = PacienteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Documento já cadastrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<PacienteResponse> atualizar(
            @Parameter(description = "ID do paciente.", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Campos para atualização parcial.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PacienteUpdateRequest.class))
            )
            @RequestBody @Valid PacienteUpdateRequest request){
        return ResponseEntity.ok(pacienteService.atualizar(id, request));
    }

    @Operation(summary = "Excluir paciente", description = "Remove o paciente pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paciente removido"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do paciente.", required = true, example = "1")
            @PathVariable Long id){
        pacienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
