package com.example.agendamento_consultas.controller;

import com.example.agendamento_consultas.database.enums.TipoConsulta;
import com.example.agendamento_consultas.dto.request.AgendamentoCreateRequest;
import com.example.agendamento_consultas.dto.request.AgendamentoUpdateRequest;
import com.example.agendamento_consultas.dto.request.ReagendamentoRequest;
import com.example.agendamento_consultas.dto.response.AgendamentoResponse;
import com.example.agendamento_consultas.dto.response.PageResponse;
import com.example.agendamento_consultas.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @PostMapping
    public ResponseEntity<AgendamentoResponse> criar(
            @RequestBody @Valid AgendamentoCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.criar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<AgendamentoResponse>> listar(
            @RequestParam(required = false) TipoConsulta tipoConsulta,
            @PageableDefault(size = 20, sort = "data", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<AgendamentoResponse> page = agendamentoService.listar(tipoConsulta, pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AgendamentoUpdateRequest request) {

        return ResponseEntity.ok(agendamentoService.atualizar(id, request));
    }

    @PatchMapping("/{id}/reagendamento")
    public ResponseEntity<AgendamentoResponse> reagendar(
            @PathVariable Long id,
            @RequestBody @Valid ReagendamentoRequest request) {

        return ResponseEntity.ok(agendamentoService.reagendar(id, request));
    }

    @PatchMapping("/{id}/cancelamento")
    public ResponseEntity<AgendamentoResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.cancelar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        agendamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
