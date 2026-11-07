package com.upc.compraamigo.dtos;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private double co2;
    private String imagen;
    private String tallas;
    private CategoriaDTO categoria;
}
