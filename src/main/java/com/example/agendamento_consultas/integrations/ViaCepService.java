package com.example.agendamento_consultas.integrations;

import com.example.agendamento_consultas.clients.ViaCepClient;
import com.example.agendamento_consultas.dto.response.ViaCepResponse;
import com.example.agendamento_consultas.exception.CepNotFoundException;
import com.example.agendamento_consultas.exception.InvalidCepFormatException;
import com.example.agendamento_consultas.exception.ServiceUnavailableException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViaCepService {

    private final ViaCepClient client;

    public ViaCepResponse buscarPorCep(String cep) {
        String cepFormatado = processarCep(cep);

        try {
            ViaCepResponse response = client.buscarPorCep(cepFormatado);

            if (response == null) {
                throw new ServiceUnavailableException("Serviço do ViaCEP indisponível");
            }

            if (Boolean.TRUE.equals(response.erro())) {
                throw new CepNotFoundException("CEP não encontrado");
            }

            return response;

        } catch (FeignException ex) {
            log.error("Erro ao consultar o ViaCEP", ex);
            throw new ServiceUnavailableException("Erro ao consultar o ViaCEP: serviço indisponível");
        }
    }

    private String processarCep(String cep) {
        String cepFormatado = cep.replaceAll("\\D", "");

        if (!cepFormatado.matches("\\d{8}")) {
            throw new InvalidCepFormatException("CEP deve conter exatamente 8 números");
        }

        return cepFormatado;
    }
}