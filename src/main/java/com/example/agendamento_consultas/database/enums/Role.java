package com.example.agendamento_consultas.database.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Perfis de autorização do sistema.")
public enum Role {
    ROLE_ADMIN,
    ROLE_MEDICO
}
