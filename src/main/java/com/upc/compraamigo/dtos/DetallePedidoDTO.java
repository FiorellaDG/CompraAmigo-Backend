package com.upc.compraamigo.dtos;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoDTO {
    private ProductoReferencia producto;
    private int cantidad;
    private String talla;

    public record ProductoReferencia(Long id) {}
}
