package com.example.agendamento_consultas.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityAndErrorContractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveRetornarJsonQuandoNaoAutenticado() throws Exception {
        mockMvc.perform(get("/v1/pacientes"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Nao autenticado"));
    }

    @Test
    @WithMockUser(username = "medico@clinica.com", roles = "MEDICO")
    void deveRetornarJsonQuandoAcessoForNegado() throws Exception {
        String body = """
                {
                  "email":"novo@clinica.com",
                  "senha":"Senha@123"
                }
                """;

        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Acesso negado"));
    }

    @Test
    void deveRetornarJsonQuandoFalharValidacao() throws Exception {
        String body = """
                {
                  "email":"",
                  "senha":""
                }
                """;

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(not(blankOrNullString())));
    }

    @Test
    void deveRetornarJsonQuandoBootstrapKeyForInvalida() throws Exception {
        String body = """
                {
                  "email":"admin@clinica.com",
                  "senha":"Senha@123"
                }
                """;

        mockMvc.perform(post("/v1/auth/bootstrap-admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Bootstrap-Key", "chave-invalida")
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Bootstrap key invalida"));
    }

    @Test
    void deveRetornarJsonQuandoTokenForInvalido() throws Exception {
        mockMvc.perform(get("/v1/pacientes")
                        .header("Authorization", "Bearer token-invalido"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Token invalido"));
    }
}

