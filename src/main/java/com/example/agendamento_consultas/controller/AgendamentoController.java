package com.example.agendamento_consultas.controller;

import com.example.agendamento_consultas.config.OpenApiConfig;
import com.example.agendamento_consultas.database.enums.TipoConsulta;
import com.example.agendamento_consultas.dto.request.AgendamentoCreateRequest;
import com.example.agendamento_consultas.dto.request.AgendamentoUpdateRequest;
import com.example.agendamento_consultas.dto.request.ReagendamentoRequest;
import com.example.agendamento_consultas.dto.response.AgendamentoResponse;
import com.example.agendamento_consultas.dto.response.PageResponse;
import com.example.agendamento_consultas.controller.docs.AgendamentoControllerDocs;
import com.example.agendamento_consultas.service.AgendamentoService;
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
@RequestMapping("/v1/agendamentos")
@RequiredArgsConstructor
@Tag(name = "Agendamentos", description = "Gestao de agendamentos de consultas.")
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_SCHEME)
public class AgendamentoController implements AgendamentoControllerDocs {

    private final AgendamentoService agendamentoService;

    @PostMapping
    @Override
    public ResponseEntity<AgendamentoResponse> criar(@RequestBody @Valid AgendamentoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.criar(request));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<AgendamentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.buscarPorId(id));
    }

    @GetMapping
    @Override
    public ResponseEntity<PageResponse<AgendamentoResponse>> listar(
            @RequestParam(required = false) TipoConsulta tipoConsulta,
            @PageableDefault(size = 20, sort = "data", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<AgendamentoResponse> page = agendamentoService.listar(tipoConsulta, pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @PatchMapping("/{id}")
    @Override
    public ResponseEntity<AgendamentoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AgendamentoUpdateRequest request) {

        return ResponseEntity.ok(agendamentoService.atualizar(id, request));
    }

    @PatchMapping("/{id}/reagendamento")
    @Override
    public ResponseEntity<AgendamentoResponse> reagendar(
            @PathVariable Long id,
            @RequestBody @Valid ReagendamentoRequest request) {

        return ResponseEntity.ok(agendamentoService.reagendar(id, request));
    }

    @PatchMapping("/{id}/cancelamento")
    @Override
    public ResponseEntity<AgendamentoResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.cancelar(id));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        agendamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
