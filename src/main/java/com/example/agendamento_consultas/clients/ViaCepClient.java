package com.example.agendamento_consultas.clients;

import com.example.agendamento_consultas.dto.response.ViaCepResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "via-cep", url = "${viacep.url}")
public interface ViaCepClient {
    @GetMapping("/{cep}/json")
    ViaCepResponse buscarPorCep(@PathVariable String cep);
}
