package com.example.Essenza.config;

import com.example.Essenza.security.CustomUserDetailsService;
import com.example.Essenza.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Habilita la seguridad en la aplicación
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Permite usar @PreAuthorize (Punto 9a)
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // Crea el filtro JWT
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    // Bean para el cifrado de contraseñas (Punto 9)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean para el AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Configuración de las reglas de seguridad
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Deshabilita CSRF para APIs REST
            // Configuración de la sesión (Stateless para JWT - Punto 9c)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                
                // 1. Acceso a la raíz y rutas de error
                .requestMatchers("/", "/error").permitAll()
                
                // 2. Acceso público para Auth: /api/v1/auth/login y /api/v1/auth/register
                .requestMatchers("/api/v1/auth/**").permitAll()
                
                // 3. Permite acceso a Swagger/OpenAPI (Punto 8)
                // Usamos un array más amplio para cubrir todas las subrutas de Swagger
                .requestMatchers(
                    "/v3/api-docs/**", // Rutas de documentación JSON/YAML
                    "/swagger-ui/**",  // Rutas de la UI de Swagger
                    "/swagger-ui.html" // Ruta principal
                ).permitAll()
                
                // 4. Permite acceso al GET de Productos (Lectura pública)
                .requestMatchers(HttpMethod.GET, "/api/v1/productos/**").permitAll()
                
                // --- RESTRICCIONES POR ROL (Punto 9a) ---
                
                // Restricción para crear, actualizar y eliminar productos (Requiere ADMIN)
                .requestMatchers(HttpMethod.POST, "/api/v1/productos/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/productos/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/productos/**").hasAuthority("ADMIN")
                
                // Cualquier otra solicitud requiere autenticación
                .anyRequest().authenticated()
            );

        // Agrega el filtro JWT antes del filtro de usuario/contraseña de Spring Security
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}