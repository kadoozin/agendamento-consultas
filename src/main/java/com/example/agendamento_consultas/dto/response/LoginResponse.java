package com.example.agendamento_consultas.dto.response;

public record LoginResponse(
        String token,
        String refreshToken,
        String tokenType,
        Long expiresIn
) {
}
