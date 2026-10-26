package com.eiba.System_Finances.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("CSC - Sistema de Gestão Escolar")
                .description("API REST para gestão de alunos, turmas, pagamentos e controle financeiro do Colégio Silva Carvalho.")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Ayrton Carvalho")
                    .url("https://github.com/AyrtonCarvalh0")))
            .addSecurityItem(new SecurityRequirement().addList("Bearer JWT"))
            .components(new Components()
                .addSecuritySchemes("Bearer JWT",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Cole o token JWT retornado pelo /auth/login")));
    }
}
