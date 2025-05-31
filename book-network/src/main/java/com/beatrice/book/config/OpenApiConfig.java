package com.beatrice.book.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Beatrice",
                        email = "beatrice.blidaru21@gmail.com",
                        url = "https://www.linkedin.com/in/ioana-beatrice-blidaru-419a3b211/"
                ),
                description = "OpenApi documentation for Spring security",
                title = "OpenApi specification - Beatrice",
                version = "1.0", //versiunea pt API
                license = @License(
                        name = "Disertatie",
                        url = "https://disertatie-url.com"
                ),
                termsOfService = "Terms of service"
        ),
        // trebuie sa ofer o lista de servere
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8088/api/v1" // asta e base url, de restul se ocupa swagger ul
                ),
                @Server(
                        description = "PROD ENV",
                        url = "https://disertatie-url.com" // nu e real, e doar de test
                )
        },
        // cea mai importanta parte, securitatea
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
// folosim aceeasi schema pt toate controllerele si toate resources
@SecurityScheme(
        name = "bearerAuth", // la fel ca numele pt security
        description = "JWT auth description",
        scheme = "bearer", // schema e de tip bearer token
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER // unde vreau sa includ bearer token
)
public class OpenApiConfig {
}
