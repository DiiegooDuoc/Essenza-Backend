package com.example.Essenza.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

// Configuración general de la API
@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Essenza Perfumes API", 
        version = "v1", 
        description = "Documentación de la API REST para la tienda Essenza. Proyecto Fullstack II."
))
// Define el esquema de seguridad JWT (Punto 9)
@SecurityScheme(
    name = "bearerAuth", // Nombre de referencia que usa el código de seguridad
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Token JWT para acceso a rutas restringidas (Rol ADMIN)."
)
public class OpenApiConfig {
    // Esta clase solo se usa para las anotaciones de configuración
}