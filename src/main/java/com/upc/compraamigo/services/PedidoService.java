package com.upc.compraamigo.services;

import com.upc.compraamigo.dtos.CrearPedidoRequestDTO;
import com.upc.compraamigo.dtos.PedidoDTO;

import java.util.List;

public interface PedidoService {
    record TotalCheckout(double subtotal, double costoEnvio, double total) {}
    TotalCheckout cotizarCarrito(Long userId, String envio);
    PedidoDTO confirmarCarrito(CrearPedidoRequestDTO requestDTO);
    boolean crearPedido(CrearPedidoRequestDTO requestDTO);
    List<PedidoDTO> obtenerTodosLosPedidos();
    PedidoDTO obtenerPedidoPorId(Long pedidoId);
    List<PedidoDTO> obtenerPedidosPorUsuario(Long userId);
    PedidoDTO actualizarEstado(Long pedidoId, String estado);
}
