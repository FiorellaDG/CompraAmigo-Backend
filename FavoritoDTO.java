package com.upc.compraamigo.dtos;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FavoritoDTO {

    private Long id;
    private Long userID;
    private Long productoID;

    private String nombreProducto;
    private double precio;
    private double co2;
}