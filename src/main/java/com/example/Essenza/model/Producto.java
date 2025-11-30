package com.example.Essenza.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "PRODUCTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_id")
    private Long id; 

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "precio", nullable = false)
    private Double precio; 

    @Column(name = "stock", nullable = false)
    private Integer stock; 

    @Column(name = "imagen_url")
    private String imagenUrl;

    // Relación N:1 con CATEGORIA (FK: CATEGORIA_categoria_id)
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "CATEGORIA_categoria_id", nullable = false)
    private Categoria categoria;
}