package fr.periscol.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@EnableWebMvc
@Component
public class SwaggerConfiguration extends WebMvcConfigurationSupport {

    /*private final String description;

    @Bean
    public OpenAPI customOpenAPI(@Value("${minecraft-data.api-version}") String appVersion) {
        return new OpenAPI()
                .info(new Info()
                        .title("Minecraft Resource API")
                        .version(appVersion)
                        .description(description)

                );
    }

    public SwaggerConfiguration() {
        final var opt = Utils.readResourceFile("description");
        if (opt.isEmpty())
            throw new NullPointerException("'description' file doesn't exist in resources");
        description = opt.get();
    }

    /**
     * {@inheritDoc}
     */
    /*
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
    */
}
