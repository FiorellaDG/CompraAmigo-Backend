package com.upc.compraamigo.dtos;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImpactoUsuarioDTO {
    private Long userID;
    private long totalPedidos;
    private long productosComprados;
    private double co2Ahorrado;
    private double arbolesEquivalentes;
}
