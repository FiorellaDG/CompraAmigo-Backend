package com.upc.compraamigo.dtos;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImpactoCategoriaDTO {
    private String categoria;
    private long unidadesVendidas;
    private double co2Ahorrado;
    private String estadoOds;
}
