package com.upc.compraamigo.entities;

import com.upc.compraamigo.security.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "consultas_chat")
 
public class ConsultaChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;
    private String consulta;
    private String respuesta;
    private String resultado;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
