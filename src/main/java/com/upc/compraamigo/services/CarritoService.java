package com.upc.compraamigo.services;

import com.upc.compraamigo.dtos.CarritoItemDTO;

import java.util.List;

public interface CarritoService {
    CarritoItemDTO agregarProducto(
            CarritoItemDTO carritoItemDTO
    );
    List<CarritoItemDTO> listarCarrito(
            Long userId
    );
    CarritoItemDTO modificarCantidad(
            Long detalleId,
            Integer cantidad
    );
    void eliminarProducto(
            Long detalleId
    );
}