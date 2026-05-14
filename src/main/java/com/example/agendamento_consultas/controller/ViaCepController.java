package com.example.agendamento_consultas.controller;

import com.example.agendamento_consultas.config.OpenApiConfig;
import com.example.agendamento_consultas.controller.docs.ViaCepControllerDocs;
import com.example.agendamento_consultas.dto.response.ViaCepResponse;
import com.example.agendamento_consultas.integrations.ViaCepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/enderecos")
@RequiredArgsConstructor
@Tag(name = "Enderecos", description = "Consulta de enderecos via integracao com ViaCEP.")
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_SCHEME)
public class ViaCepController implements ViaCepControllerDocs {

    private final ViaCepService viaCepService;

    @GetMapping("/cep/{cep}")
    @Override
    public ResponseEntity<ViaCepResponse> buscarPorCep(@PathVariable String cep) {
        return ResponseEntity.ok(viaCepService.buscarPorCep(cep));
    }
}
