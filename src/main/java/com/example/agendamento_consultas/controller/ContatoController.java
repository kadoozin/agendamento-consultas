package com.example.agendamento_consultas.controller;

import com.example.agendamento_consultas.config.OpenApiConfig;
import com.example.agendamento_consultas.dto.request.ContatoCreateRequest;
import com.example.agendamento_consultas.dto.request.ContatoUpdateRequest;
import com.example.agendamento_consultas.dto.response.ApiErrorResponse;
import com.example.agendamento_consultas.dto.response.ContatoPageResponse;
import com.example.agendamento_consultas.dto.response.ContatoResponse;
import com.example.agendamento_consultas.dto.response.PageResponse;
import com.example.agendamento_consultas.service.ContatoService;
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
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/contatos")
@RequiredArgsConstructor
@Tag(name = "Contatos", description = "Gestão de meios de contato dos pacientes.")
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_SCHEME)
public class ContatoController {

    private final ContatoService contatoService;

    @Operation(summary = "Criar contato", description = "Cadastra um contato associado a um paciente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contato criado",
                    content = @Content(schema = @Schema(implementation = ContatoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email ou número já cadastrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ContatoResponse> criar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para criação de contato.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ContatoCreateRequest.class))
            )
            @RequestBody @Valid ContatoCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(contatoService.criar(request));
    }

    @Operation(summary = "Buscar contato por ID", description = "Retorna um contato específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contato encontrado",
                    content = @Content(schema = @Schema(implementation = ContatoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Contato não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ContatoResponse> buscarPorId(
            @Parameter(description = "ID do contato.", required = true, example = "15")
            @PathVariable Long id){
        return ResponseEntity.ok(contatoService.buscarPorId(id));
    }

    @Operation(summary = "Buscar contato por email", description = "Retorna contato a partir do email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contato encontrado",
                    content = @Content(schema = @Schema(implementation = ContatoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Contato não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<ContatoResponse> buscarPorEmail(
            @Parameter(description = "Email do contato.", required = true, example = "maria@email.com")
            @PathVariable String email){
        return ResponseEntity.ok(contatoService.buscarPorEmail(email));
    }

    @Operation(summary = "Buscar contato por número", description = "Retorna contato a partir do número telefônico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contato encontrado",
                    content = @Content(schema = @Schema(implementation = ContatoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Contato não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/numero/{numero}")
    public ResponseEntity<ContatoResponse> buscarPorNumero(
            @Parameter(description = "Número de telefone do contato.", required = true, example = "+5511999999999")
            @PathVariable String numero){
        return ResponseEntity.ok(contatoService.buscarPorNumero(numero));
    }

    @Operation(summary = "Listar contatos", description = "Lista contatos com paginação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = ContatoPageResponse.class)))
    })
    @GetMapping
    public ResponseEntity<PageResponse<ContatoResponse>> listar(
            @ParameterObject
            @PageableDefault(size = 20, sort = "id") Pageable pageable){

        Page<ContatoResponse> page = contatoService.listarTodos(pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @Operation(summary = "Atualizar contato", description = "Atualiza os dados de um contato existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contato atualizado",
                    content = @Content(schema = @Schema(implementation = ContatoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Contato ou paciente não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email ou número já cadastrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ContatoResponse> atualizar(
            @Parameter(description = "ID do contato.", required = true, example = "15")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para atualização do contato.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ContatoUpdateRequest.class))
            )
            @RequestBody @Valid ContatoUpdateRequest request){

        return ResponseEntity.ok(contatoService.atualizar(id, request));
    }

    @Operation(summary = "Excluir contato", description = "Remove um contato pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Contato removido"),
            @ApiResponse(responseCode = "404", description = "Contato não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do contato.", required = true, example = "15")
            @PathVariable Long id){
        contatoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
