package com.upc.compraamigo.services;

import com.upc.compraamigo.dtos.ProductoDTO;
import java.util.List;

public interface ProductoService {
    ProductoDTO insertarProducto(ProductoDTO productoDTO);
    void eliminarProducto(Long id);
    ProductoDTO modificarProducto(ProductoDTO productoDTO);
    List<ProductoDTO> listarProductos();
    ProductoDTO buscarProductoPorId(long id);
    List<ProductoDTO> buscarProductos(String nombre, Long categoriaId, Double precioMin, Double precioMax);
}
