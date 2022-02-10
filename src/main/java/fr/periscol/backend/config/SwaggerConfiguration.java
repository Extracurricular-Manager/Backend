package fr.periscol.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@EnableWebMvc
@Component
public class SwaggerConfiguration extends WebMvcConfigurationSupport {

    @Bean
    public OpenAPI customOpenAPI(@Value("${periscol.api-version}") String appVersion) {
        return new OpenAPI()
                .info(new Info()
                        .title("Periscol API")
                        .version(appVersion)
                );
    }
}
