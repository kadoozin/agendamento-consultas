package com.example.agendamento_consultas.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LoginResponse", description = "Resposta de autenticação contendo tokens.")
public record LoginResponse(
        @Schema(description = "Token JWT de acesso.", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,
        @Schema(description = "Refresh token para renovação da sessão.", example = "f3f3f9ba-7b4d-4bde-b61d-3de4c86d0d4e")
        String refreshToken,
        @Schema(description = "Tipo do token de acesso.", example = "Bearer")
        String tokenType,
        @Schema(description = "Tempo de expiração do token de acesso em milissegundos.", example = "86400000")
        Long expiresIn
) {
}
