package com.example.Essenza.repository;

import com.example.Essenza.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Hereda los métodos CRUD
}