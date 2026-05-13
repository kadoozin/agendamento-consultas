package com.example.agendamento_consultas.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "RefreshTokenRequest", description = "Payload para renovação do token de acesso.")
public record RefreshTokenRequest(
        @Schema(description = "Refresh token ativo.", example = "f3f3f9ba-7b4d-4bde-b61d-3de4c86d0d4e")
        @NotBlank String refreshToken
) {
}
