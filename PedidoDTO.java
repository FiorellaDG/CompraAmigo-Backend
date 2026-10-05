package com.upc.compraamigo.dtos;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private Long pedidoId;
    private LocalDate fecha;
    private String estado;

    private Long userID;
    private String nombreUsuario;

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

    private List<DetallePedidoResponseDTO> detalles = new ArrayList<>();
}
