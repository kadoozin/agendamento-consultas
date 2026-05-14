package com.example.agendamento_consultas.exception.handler;

import com.example.agendamento_consultas.dto.response.ApiErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void deveResponder409QuandoHouverFalhaDeConcorrencia() {
        ResponseEntity<ApiErrorResponse> response =
                handler.handleConcurrencyFailure(new ConcurrencyFailureException("deadlock"));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Conflito de concorrencia. Tente novamente.", response.getBody().message());
    }
}

