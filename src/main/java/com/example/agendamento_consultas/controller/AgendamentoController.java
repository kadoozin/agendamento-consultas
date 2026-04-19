package com.example.agendamento_consultas.controller;

import com.example.agendamento_consultas.database.enums.TipoConsulta;
import com.example.agendamento_consultas.dto.request.AgendamentoCreateRequest;
import com.example.agendamento_consultas.dto.request.AgendamentoUpdateRequest;
import com.example.agendamento_consultas.dto.response.AgendamentoResponse;
import com.example.agendamento_consultas.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {
    private final AgendamentoService agendamentoService;

    @PostMapping
    public ResponseEntity<AgendamentoResponse> criar(@RequestBody @Valid AgendamentoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.criar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoResponse>> listar(
            @RequestParam(required = false) TipoConsulta tipoConsulta) {
        return ResponseEntity.ok(agendamentoService.listar(tipoConsulta));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid AgendamentoUpdateRequest request) {
        return ResponseEntity.ok(agendamentoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        agendamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
