package com.example.agendamento_consultas.controller;

import com.example.agendamento_consultas.dto.request.ContatoRequest;
import com.example.agendamento_consultas.dto.response.ContatoResponse;
import com.example.agendamento_consultas.service.ContatoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/contatos")
@RequiredArgsConstructor
public class ContatoController {
    private final ContatoService contatoService;

    @PostMapping
    public ResponseEntity<ContatoResponse> criar(@RequestBody @Valid ContatoRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(contatoService.criar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContatoResponse> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(contatoService.buscarPorId(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ContatoResponse> buscarPorEmail(@PathVariable String email){
        return ResponseEntity.ok(contatoService.buscarPorEmail(email));
    }

    @GetMapping("/numero/{numero}")
    public ResponseEntity<ContatoResponse> buscarPorNumero(@PathVariable String numero){
        return ResponseEntity.ok(contatoService.buscarPorNumero(numero));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ContatoResponse>> listar(){
        return ResponseEntity.ok(contatoService.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContatoResponse> atualizar(@PathVariable Long id, ContatoRequest request){
        return ResponseEntity.ok(contatoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        contatoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
