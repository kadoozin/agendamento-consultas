package com.example.agendamento_consultas.controller;

import com.example.agendamento_consultas.database.enums.TipoConsulta;
import com.example.agendamento_consultas.dto.request.AgendamentoRequest;
import com.example.agendamento_consultas.dto.request.AtualizarStatusRequest;
import com.example.agendamento_consultas.dto.request.AtualizarTipoConsultaRequest;
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
    public ResponseEntity<AgendamentoResponse> criar(@RequestBody @Valid AgendamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.criar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoResponse>> listarTodos(){
        return ResponseEntity.ok(agendamentoService.listarTodos());
    }

    @GetMapping("/tipo/{tipoConsulta}")
    public ResponseEntity<List<AgendamentoResponse>> listarPorTipo(@PathVariable TipoConsulta tipoConsulta){
        return ResponseEntity.ok(agendamentoService.listarPorTipo(tipoConsulta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid AgendamentoRequest request){
        return ResponseEntity.ok(agendamentoService.atualizar(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AgendamentoResponse> atualizarStatus(@PathVariable Long id,
                                                               @RequestBody @Valid AtualizarStatusRequest request){
        return ResponseEntity.ok(agendamentoService.atualizarStatus(id, request));
    }

    @PatchMapping("/{id}/tipo-consulta")
    public ResponseEntity<AgendamentoResponse> atualizarTipoConsulta(@PathVariable Long id,
                                                                     @RequestBody @Valid AtualizarTipoConsultaRequest request){
        return ResponseEntity.ok(agendamentoService.atualizarTipoConsulta(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        agendamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
