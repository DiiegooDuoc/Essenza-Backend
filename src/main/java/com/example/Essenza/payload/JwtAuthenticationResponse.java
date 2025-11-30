package com.example.Essenza.payload;

import lombok.Data;

// Objeto para devolver el Token JWT al frontend después de un login exitoso
@Data
public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String role; // Devolvemos el rol para facilitar la restricción en frontend (Punto 9d)

    public JwtAuthenticationResponse(String accessToken, String role) {
        this.accessToken = accessToken;
        this.role = role;
    }
}