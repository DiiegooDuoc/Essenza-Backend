package com.example.Essenza.payload;

import lombok.Data;

// Objeto para recibir datos en el endpoint /register
@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String nombre;
    private String apellido;
    private Integer telefono;
    private String direccion;
}