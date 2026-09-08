package com.phabdev.apistarter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiStarterOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Spring Boot API Starter - Free Edition")
                .description("A clean and minimal Spring Boot REST API starter template.")
                .version("1.0.0")
                .contact(new Contact().name("Fabrizio Ferrante (PHABDEV)"))
                .license(new License().name("MIT")));
    }
}
