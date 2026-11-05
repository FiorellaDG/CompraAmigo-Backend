package com.upc.compraamigo.entities;

import com.upc.compraamigo.security.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pedido_id")
    private Long pedidoId;

    private LocalDate fecha;
    private String estado;

    private String nombreReceptor;
    private String telefono;
    private String ciudad;
    private String distrito;
    private String direccionEnvio;

    private String metodoEnvio;
    private double costoEnvio;
    private String metodoPago;

    private double subtotal;
    private double total;
    private double co2Ahorrado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
