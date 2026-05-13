package com.example.agendamento_consultas.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final String BEARER_AUTH_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI agendamentoOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Agendamento de Consultas")
                        .version("v1")
                        .description("API para gestão de pacientes, contatos, agendamentos e autenticação.")
                        .contact(new Contact()
                                .name("Equipe Agendamento")
                                .email("suporte@agendamento.local"))
                        .license(new License()
                                .name("Proprietary")))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH_SCHEME,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
