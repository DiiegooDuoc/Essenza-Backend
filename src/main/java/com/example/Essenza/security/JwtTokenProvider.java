package com.example.Essenza.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Base64; // <-- IMPORTACIÓN NECESARIA PARA LA SOLUCIÓN

// Clase que maneja la Generación, Verificación y Renovación de Tokens JWT (Punto 9b)
@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;
    
    // CORRECCIÓN CLAVE: Codificamos la clave secreta a Base64 para asegurar la longitud
    private Key getSigningKey() {
        // La clave ahora se codifica para cumplir con el estándar HS512 y evitar el error de JWT
        byte[] keyBytes = Base64.getEncoder().encode(jwtSecret.getBytes());
        return Keys.hmacShaKeyFor(keyBytes); 
    }

    // Genera el token JWT a partir de la autenticación del usuario
    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        // Aseguramos que el rol siempre se obtenga (el usuario ya está autenticado)
        String role = userPrincipal.getAuthorities().iterator().next().getAuthority();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername()) // Email del usuario
                .claim("role", role) // Agrega el rol al token
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    
    // Obtiene el email (subject) del token JWT
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // Valida el token JWT (Punto 9b)
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            // Manejar errores de token (expirado, inválido, etc.)
        }
        return false;
    }
}