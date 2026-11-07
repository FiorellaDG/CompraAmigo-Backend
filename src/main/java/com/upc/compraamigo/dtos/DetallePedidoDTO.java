package com.upc.compraamigo.dtos;

import com.upc.compraamigo.entities.Producto;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoDTO {
    private Producto producto = new Producto();
    private int cantidad;
    private String talla;
}
