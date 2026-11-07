package com.upc.compraamigo.entities;

import com.upc.compraamigo.security.entities.User;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "favoritos",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "producto_id"})
        }
)

public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
}