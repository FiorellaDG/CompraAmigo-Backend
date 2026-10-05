package com.upc.compraamigo.dtos;

import lombok.*;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaChatDTO {
    private Long id;
    private LocalDateTime fecha;
    private String consulta;
    private String respuesta;
    private String resultado;
    private Long userID;
}
