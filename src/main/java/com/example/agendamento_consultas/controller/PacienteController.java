package com.example.agendamento_consultas.controller;

import com.example.agendamento_consultas.dto.request.PacienteCreateRequest;
import com.example.agendamento_consultas.dto.request.PacienteUpdateRequest;
import com.example.agendamento_consultas.dto.response.PacienteResponse;
import com.example.agendamento_consultas.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/pacientes")
@RequiredArgsConstructor
public class PacienteController {
    private final PacienteService pacienteService;

    @PostMapping
    public ResponseEntity<PacienteResponse> criar(@RequestBody @Valid PacienteCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.criar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponse> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(pacienteService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<PacienteResponse>> buscar(
            @RequestParam(required = false) String nomeCompleto,
            @RequestParam(required = false) String documentoIdentificacao){
        return ResponseEntity.ok(pacienteService.buscar(nomeCompleto, documentoIdentificacao));
    }

    @GetMapping
    public ResponseEntity<List<PacienteResponse>> listar(){
        return ResponseEntity.ok(pacienteService.listar());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PacienteResponse> atualizar(@PathVariable Long id, @RequestBody @Valid PacienteUpdateRequest request){
        return ResponseEntity.ok(pacienteService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        pacienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
