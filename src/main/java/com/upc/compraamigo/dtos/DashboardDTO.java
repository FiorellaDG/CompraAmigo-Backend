package com.upc.compraamigo.dtos;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private double ventasDia;
    private double ventasMes;
    private long totalPedidos;
    private long pedidosEnPreparacion;
    private long totalProductos;
    private long totalConsultas;
}
