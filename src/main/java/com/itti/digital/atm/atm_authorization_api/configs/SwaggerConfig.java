package com.itti.digital.atm.atm_authorization_api.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

/**
 * @author Giancarlo Migliore
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Services ATM",
                version = "1.0",
                description = "API creado para exponer servicios relacionados a los ATM",
                contact = @Contact(url = "https://www.itti.digital", name = "Giancarlo Migliore", email = "giancarlo.migliore@itti.digital")
        ),
        tags = {
                @Tag(name = "products", description = "Servicios para otencion de productos por CI"),
              //todo  @Tag(name = "persons", description = "Servicios datos de persona"),
                @Tag(name = "auth", description = "Servicios de autenticacion de usuarios"),
        }
)
@Configuration
public class SwaggerConfig {
        @Bean
        ForwardedHeaderFilter forwardedHeaderFilter() {
                return new ForwardedHeaderFilter();
        }
}
