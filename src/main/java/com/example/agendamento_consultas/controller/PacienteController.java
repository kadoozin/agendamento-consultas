package com.example.agendamento_consultas.controller;

import com.example.agendamento_consultas.config.OpenApiConfig;
import com.example.agendamento_consultas.controller.docs.PacienteControllerDocs;
import com.example.agendamento_consultas.dto.request.PacienteCreateRequest;
import com.example.agendamento_consultas.dto.request.PacienteUpdateRequest;
import com.example.agendamento_consultas.dto.response.PageResponse;
import com.example.agendamento_consultas.dto.response.PacienteResponse;
import com.example.agendamento_consultas.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/pacientes")
@RequiredArgsConstructor
@Tag(name = "Pacientes", description = "Gestao de cadastro e consulta de pacientes.")
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_SCHEME)
public class PacienteController implements PacienteControllerDocs {
    private final PacienteService pacienteService;

    @PostMapping
    @Override
    public ResponseEntity<PacienteResponse> criar(@RequestBody @Valid PacienteCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.criar(request));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<PacienteResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    @Override
    public ResponseEntity<PageResponse<PacienteResponse>> buscar(
            @RequestParam(required = false) String nomeCompleto,
            @RequestParam(required = false) String documentoIdentificacao,
            @PageableDefault(size = 20, sort = "nomeCompleto", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<PacienteResponse> page = pacienteService.buscar(nomeCompleto, documentoIdentificacao, pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @GetMapping
    @Override
    public ResponseEntity<PageResponse<PacienteResponse>> listar(
            @PageableDefault(size = 20, sort = "nomeCompleto") Pageable pageable) {

        Page<PacienteResponse> page = pacienteService.listar(pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @PatchMapping("/{id}")
    @Override
    public ResponseEntity<PacienteResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid PacienteUpdateRequest request) {
        return ResponseEntity.ok(pacienteService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pacienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
