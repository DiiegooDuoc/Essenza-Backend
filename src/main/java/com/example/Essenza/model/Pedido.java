package com.example.Essenza.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "PEDIDO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pedido_id")
    private Long id;

    @Column(name = "fecha_pedido", nullable = false)
    private Date fechaPedido = new Date();

    @Column(name = "total", nullable = false)
    private Double total;

    @Column(name = "estado", nullable = false)
    private String estado; // Ej: "PENDIENTE", "ENVIADO", "ENTREGADO"

    // Relación N:1 con USUARIO (FK: USUARIO_usuario_id en esta tabla)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIO_usuario_id", nullable = false)
    private Usuario usuario;

    // Relación 1:N con DETALLE_PEDIDO
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DetallePedido> detalles = new HashSet<>();
}