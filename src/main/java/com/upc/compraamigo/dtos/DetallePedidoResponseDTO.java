package com.upc.compraamigo.dtos;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoResponseDTO {
    private Long detalleId;
    private Long productoID;
    private String nombreProducto;
    private int cantidad;
    private String talla;
    private double precioUnitario;
    private double co2Unitario;
}
