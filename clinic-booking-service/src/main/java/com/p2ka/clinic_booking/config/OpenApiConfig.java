package com.p2ka.clinic_booking.config;

import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/*@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Alibou",
                        email = "contact@aliboucoding.com",
                        url = "https://aliboucoding.com/course"
                ),
                description = "OpenApi documentation for Spring Security",
                title = "OpenApi specification - Alibou",
                version = "1.0",
                license = @License(
                        name = "Licence name",
                        url = "https://some-url.com"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8088/api/v1"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "https://aliboucoding.com/course"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,  // Changed to use Bearer token
*//*        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
                clientCredentials =
                @OAuthFlow(
                        authorizationUrl = "http://localhost:9090/realms/book-social-network/protocol/openid-connect/auth"
                )
        ),*//*
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)*/

@Configuration
@RequiredArgsConstructor
@Profile("!prod")
public class OpenApiConfig {
    private final ApplicationConfig applicationConfig;

    public OpenAPI customOpenApi() {
        return new OpenAPI().info(new io.swagger.v3.oas.models.info.Info().title(applicationConfig.title())
                .version(applicationConfig.version()));
    }
}
