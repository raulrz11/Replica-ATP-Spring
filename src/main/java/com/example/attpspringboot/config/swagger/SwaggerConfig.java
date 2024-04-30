package com.example.attpspringboot.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
    @Bean
    OpenAPI apiInfo() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Prueba Spring Funkos")
                                .version("1.0.0")
                                .description("Api Rest con Spring sobre la ATP")
                                .contact(
                                        new Contact()
                                                .name("Raul Rodriguez Luna")
                                                .email("raul.rz.1820@gmail.com")
                                )

                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("Repositorio y Documentación del Proyecto y API")
                                .url("https://github.com/raulrz11/Prueba-Spring-Funkos")
                )
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()));
    }
    @Bean
    GroupedOpenApi httpApi(){
        return GroupedOpenApi.builder()
                .group("http")
                .pathsToMatch("/**")
                .displayName("ATP")
                .build();
    }
}
