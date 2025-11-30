package com.example.Essenza.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Entidad que representa una línea de un pedido (ej: 3 unidades de Perfume X)
@Entity
@Table(name = "DETALLE_PEDIDO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detalle_id")
    private Long id;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario_venta", nullable = false)
    private Double precioUnitarioVenta; // Precio al momento de la venta

    // Relación N:1 con PEDIDO (FK: PEDIDO_pedido_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PEDIDO_pedido_id", nullable = false)
    private Pedido pedido;

    // Relación N:1 con PRODUCTO (FK: PRODUCTO_producto_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCTO_producto_id", nullable = false)
    private Producto producto;
}