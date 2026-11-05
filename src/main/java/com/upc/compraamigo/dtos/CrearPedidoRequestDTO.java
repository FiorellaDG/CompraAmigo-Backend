package com.upc.compraamigo.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CrearPedidoRequestDTO {
    private Long userID;
    private String nombreReceptor;
    private String telefono;
    private String ciudad;
    private String distrito;
    private String direccionEnvio;
    private String metodoEnvio;
    private String metodoPago;
    private List<DetallePedidoDTO> detalles = new ArrayList<>();
}
