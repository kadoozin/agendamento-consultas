package com.example.agendamento_consultas.controller;

import com.example.agendamento_consultas.config.OpenApiConfig;
import com.example.agendamento_consultas.controller.docs.ContatoControllerDocs;
import com.example.agendamento_consultas.dto.request.ContatoCreateRequest;
import com.example.agendamento_consultas.dto.request.ContatoUpdateRequest;
import com.example.agendamento_consultas.dto.response.ContatoResponse;
import com.example.agendamento_consultas.dto.response.PageResponse;
import com.example.agendamento_consultas.service.ContatoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/contatos")
@RequiredArgsConstructor
@Tag(name = "Contatos", description = "Gestao de meios de contato dos pacientes.")
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_SCHEME)
public class ContatoController implements ContatoControllerDocs {

    private final ContatoService contatoService;

    @PostMapping
    @Override
    public ResponseEntity<ContatoResponse> criar(@RequestBody @Valid ContatoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contatoService.criar(request));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ContatoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(contatoService.buscarPorId(id));
    }

    @GetMapping("/email/{email}")
    @Override
    public ResponseEntity<ContatoResponse> buscarPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(contatoService.buscarPorEmail(email));
    }

    @GetMapping("/numero/{numero}")
    @Override
    public ResponseEntity<ContatoResponse> buscarPorNumero(@PathVariable String numero) {
        return ResponseEntity.ok(contatoService.buscarPorNumero(numero));
    }

    @GetMapping
    @Override
    public ResponseEntity<PageResponse<ContatoResponse>> listar(
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {

        Page<ContatoResponse> page = contatoService.listarTodos(pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<ContatoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ContatoUpdateRequest request) {

        return ResponseEntity.ok(contatoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        contatoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
