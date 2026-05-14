package com.example.agendamento_consultas.controller.docs;

import com.example.agendamento_consultas.dto.request.ContatoCreateRequest;
import com.example.agendamento_consultas.dto.request.ContatoUpdateRequest;
import com.example.agendamento_consultas.dto.response.ApiErrorResponse;
import com.example.agendamento_consultas.dto.response.ContatoPageResponse;
import com.example.agendamento_consultas.dto.response.ContatoResponse;
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

public interface ContatoControllerDocs {

    @Operation(summary = "Criar contato", description = "Cadastra um contato associado a um paciente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contato criado",
                    content = @Content(schema = @Schema(implementation = ContatoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Paciente nao encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email ou numero ja cadastrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<ContatoResponse> criar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para criacao de contato.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ContatoCreateRequest.class))
            )
            ContatoCreateRequest request
    );

    @Operation(summary = "Buscar contato por ID", description = "Retorna um contato especifico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contato encontrado",
                    content = @Content(schema = @Schema(implementation = ContatoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Contato nao encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<ContatoResponse> buscarPorId(
            @Parameter(description = "ID do contato.", required = true, example = "15")
            Long id
    );

    @Operation(summary = "Buscar contato por email", description = "Retorna contato a partir do email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contato encontrado",
                    content = @Content(schema = @Schema(implementation = ContatoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Contato nao encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<ContatoResponse> buscarPorEmail(
            @Parameter(description = "Email do contato.", required = true, example = "maria@email.com")
            String email
    );

    @Operation(summary = "Buscar contato por numero", description = "Retorna contato a partir do numero telefonico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contato encontrado",
                    content = @Content(schema = @Schema(implementation = ContatoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Contato nao encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<ContatoResponse> buscarPorNumero(
            @Parameter(description = "Numero de telefone do contato.", required = true, example = "+5511999999999")
            String numero
    );

    @Operation(summary = "Listar contatos", description = "Lista contatos com paginacao.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = ContatoPageResponse.class)))
    })
    ResponseEntity<PageResponse<ContatoResponse>> listar(@ParameterObject Pageable pageable);

    @Operation(summary = "Atualizar contato", description = "Atualiza os dados de um contato existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contato atualizado",
                    content = @Content(schema = @Schema(implementation = ContatoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Contato ou paciente nao encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email ou numero ja cadastrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<ContatoResponse> atualizar(
            @Parameter(description = "ID do contato.", required = true, example = "15")
            Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para atualizacao do contato.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ContatoUpdateRequest.class))
            )
            ContatoUpdateRequest request
    );

    @Operation(summary = "Excluir contato", description = "Remove um contato pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Contato removido"),
            @ApiResponse(responseCode = "404", description = "Contato nao encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    ResponseEntity<Void> deletar(
            @Parameter(description = "ID do contato.", required = true, example = "15")
            Long id
    );
}
