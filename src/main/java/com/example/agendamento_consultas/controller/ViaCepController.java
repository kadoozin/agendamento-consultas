package com.example.agendamento_consultas.controller;

import com.example.agendamento_consultas.dto.response.ViaCepResponse;
import com.example.agendamento_consultas.integrations.ViaCepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/enderecos")
@RequiredArgsConstructor
public class ViaCepController {

    private final ViaCepService viaCepService;

    @GetMapping("/cep/{cep}")
    public ResponseEntity<ViaCepResponse> buscarPorCep(@PathVariable String cep){
        return ResponseEntity.ok(viaCepService.buscarPorCep(cep));
    }
}
