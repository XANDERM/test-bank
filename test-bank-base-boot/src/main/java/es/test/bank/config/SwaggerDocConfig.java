package es.test.bank.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger settings.
 */
@Configuration
class SwaggerDocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Contact Application API")
                        .license(new License().name("Test Bank Services"))
                        .version("1.0.0")
                        .description("Below is a list of available REST API calls for this \"Test Bank Services - Example\""));

    }
}
