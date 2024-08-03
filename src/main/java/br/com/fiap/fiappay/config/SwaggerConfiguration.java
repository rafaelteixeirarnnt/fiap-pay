package br.com.fiap.fiappay.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;

@Configuration
public class SwaggerConfiguration {

    private static final String BEARER_AUTHENTICATION = "Bearer Authentication";
    private static final String FIAP_PAY = "FIAP PAY";
    private static final String BEARER = "bearer";
    private static final String JWT = "JWT";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().addList(BEARER_AUTHENTICATION))
                .components(new Components().addSecuritySchemes(BEARER_AUTHENTICATION, createAPIKeyScheme()))
                .info(new Info().title(FIAP_PAY)
                        .description("Serviços disponíveis para a API FIAP PAY")
                        .version("1.0")
                        .contact(new Contact().name("Rafael Teixeira").email("rafaelteixeirarnnt@gmail.com").url("https://www.linkedin.com/in/rafael-teixeira-79161ab6/"))
                        .license(new License().name("License of API").url("API license URL")));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(HTTP)
                .bearerFormat(JWT)
                .scheme(BEARER);
    }
}
