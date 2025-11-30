package com.example.Essenza.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "USUARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long id; 

    @Column(name = "email", unique = true, nullable = false, length = 50)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash; // Contraseña cifrada (Punto 9)

    private String nombre;
    private String apellido;
    private Integer telefono; // NUMBER en BD
    private String direccion;

    // Relación N:1 con ROL (FK: ROL_rol_id está en esta tabla)
    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "ROL_rol_id", nullable = false)
    private Rol rol; 

    // Se omiten Pedido y CarritoItem por ahora para que no te den error
    // hasta que implementemos esas clases.
}