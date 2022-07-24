package com.fredfonseca.bookstoremanager.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public static final String API_DOCUMENTATION_LINK = "https://github.com/freddcf/springboot-api-bookstore";
    private static final String API_TITLE = "Bookstore Manager API";
    private static final String API_DESCRIPTION = "Bookstore Manager Professional API";
    private static final String API_VERSION = "V1.0.0";
    public static final String API_DOCUMENTATION_DESCRIPTION = "Bookstore Manager API Documentation";
    
    @Bean
    public OpenAPI bookstoreManagerAPI() {
        return new OpenAPI()
                .info(new Info().title(API_TITLE)
                .description(API_DESCRIPTION)
                .version(API_VERSION)
                .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                .description(API_DOCUMENTATION_DESCRIPTION)
                .url(API_DOCUMENTATION_LINK));
    }
}
