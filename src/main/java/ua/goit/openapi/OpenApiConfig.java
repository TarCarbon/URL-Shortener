package ua.goit.openapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title="Url-Shortner API",
                description = "Url-Shortner",
                version = "1.0.0",
                contact = @Contact(
                        name = "GitHub",
                        url = "https://github.com/TarCarbon/URL-Shortener"
                )
        )
)

// Ця секція потрібна якщо наше API захищено авторизацієй з JWT.
// Без отримання свагером JWT токена, зробити виклик так просто з нього неможна
// Усі ендпоїнти що захищені, мають маркуватися анотацією @SecurityRequirement(name="JWT")
// параметр name співпадає з тим що ми вказали в схемі.
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {
}