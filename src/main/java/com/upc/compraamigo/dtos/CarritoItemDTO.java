package com.upc.compraamigo.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarritoItemDTO {
    private Long id;
    private Long carritoID;
    private Long userID;
    private Long productoID;
    private String nombreProducto;
    private double precio;
    private Integer cantidad;
    private String talla;
    private double subtotal;
    private double co2;
}
