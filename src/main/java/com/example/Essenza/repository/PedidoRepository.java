package com.example.Essenza.repository;

import com.example.Essenza.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Aquí puedes agregar métodos para buscar pedidos por usuario o estado
}