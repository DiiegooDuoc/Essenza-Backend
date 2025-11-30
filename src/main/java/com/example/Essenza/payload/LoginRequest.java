package com.example.Essenza.payload;

import lombok.Data;

// Objeto para recibir el email y password en el endpoint /login
@Data
public class LoginRequest {
    private String email;
    private String password;
}