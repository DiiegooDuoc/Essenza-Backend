package com.example.Essenza.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Set;

@Entity
@Table(name = "ROL")
@Data // Genera Getters, Setters, etc.
@NoArgsConstructor
@AllArgsConstructor
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_id")
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre; // Ej: "CLIENTE", "ADMIN"

    @OneToMany(mappedBy = "rol", cascade = CascadeType.ALL)
    private Set<Usuario> usuarios;
}