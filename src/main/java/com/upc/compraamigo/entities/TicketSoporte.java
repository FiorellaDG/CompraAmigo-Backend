package com.upc.compraamigo.entities;

import com.upc.compraamigo.security.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets_soporte")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class TicketSoporte {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User cliente;
    @ManyToOne(fetch = FetchType.LAZY)
    private Pedido pedido;
    @Column(nullable = false, length = 120)
    private String asunto;
    @Column(nullable = false, length = 2000)
    private String mensaje;
    @Column(nullable = false)
    private LocalDateTime creado;

    public TicketSoporte(User cliente, Pedido pedido, String asunto, String mensaje) {
        this.cliente = cliente;
        this.pedido = pedido;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.creado = LocalDateTime.now();
    }
}
