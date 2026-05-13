package com.example.agendamento_consultas.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiErrorResponse", description = "Formato de resposta para erros de negócio, validação e autenticação.")
public record ApiErrorResponse(
        @Schema(
                description = "Mensagem amigável explicando o que deu errado e como interpretar o erro.",
                example = "Não encontramos um paciente com o ID informado."
        )
        String message
) {
}
