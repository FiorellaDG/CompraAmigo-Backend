package com.upc.compraamigo.serviceimpl;

import com.upc.compraamigo.dtos.ProductoDTO;
import com.upc.compraamigo.entities.Categoria;
import com.upc.compraamigo.entities.Producto;
import com.upc.compraamigo.repositories.CategoriaRepositorio;
import com.upc.compraamigo.repositories.ProductoRepositorio;
import com.upc.compraamigo.services.ProductoService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {
    @Autowired
    private ProductoRepositorio productoRepositorio;
    @Autowired
    private CategoriaRepositorio categoriaRepositorio;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    @Override
    public ProductoDTO insertarProducto(ProductoDTO productoDTO) {
        validarProducto(productoDTO);
        Categoria categoria = buscarCategoria(productoDTO);

        Producto producto = modelMapper.map(productoDTO, Producto.class);
        producto.setId(null);
        producto.setCategoria(categoria);

        return modelMapper.map(productoRepositorio.save(producto), ProductoDTO.class);
    }

    @Transactional
    @Override
    public void eliminarProducto(Long id) {
        if (!productoRepositorio.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        productoRepositorio.deleteById(id);
    }

    @Transactional
    @Override
    public ProductoDTO modificarProducto(ProductoDTO productoDTO) {
        if (productoDTO.getId() == null) {
            throw new RuntimeException("Debe indicar el ID del producto a modificar");
        }
        validarProducto(productoDTO);
        Categoria categoria = buscarCategoria(productoDTO);

        Producto existente = productoRepositorio.findById(productoDTO.getId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoDTO.getId()));

        existente.setNombre(productoDTO.getNombre());
        existente.setDescripcion(productoDTO.getDescripcion());
        existente.setPrecio(productoDTO.getPrecio());
        existente.setStock(productoDTO.getStock());
        existente.setCo2(productoDTO.getCo2());
        existente.setImagen(productoDTO.getImagen());
        existente.setTallas(productoDTO.getTallas());
        existente.setCategoria(categoria);

        return modelMapper.map(productoRepositorio.save(existente), ProductoDTO.class);
    }

    @Override
    public List<ProductoDTO> listarProductos() {
        return productoRepositorio.findAll().stream()
                .map(producto -> modelMapper.map(producto, ProductoDTO.class))
                .toList();
    }

    @Override
    public ProductoDTO buscarProductoPorId(long id) {
        return productoRepositorio.findById(id)
                .map(producto -> modelMapper.map(producto, ProductoDTO.class))
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    @Override
    public List<ProductoDTO> buscarProductos(String nombre, Long categoriaId, Double precioMin, Double precioMax) {
        if (nombre != null && nombre.isBlank()) nombre = null;
        return productoRepositorio.buscarProductos(nombre, categoriaId, precioMin, precioMax).stream()
                .map(producto -> modelMapper.map(producto, ProductoDTO.class))
                .toList();
    }

    private Categoria buscarCategoria(ProductoDTO dto) {
        if (dto.getCategoria() == null || dto.getCategoria().getId() == null) {
            throw new RuntimeException("Debe indicar una categoría válida");
        }
        return categoriaRepositorio.findById(dto.getCategoria().getId())
                .orElseThrow(() -> new RuntimeException(
                        "Categoría no encontrada con ID: " + dto.getCategoria().getId()));
    }

    private void validarProducto(ProductoDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new RuntimeException("El nombre del producto es obligatorio");
        }
        if (dto.getPrecio() < 0) {
            throw new RuntimeException("El precio no puede ser negativo");
        }
        if (dto.getStock() < 0) {
            throw new RuntimeException("El stock no puede ser negativo");
        }
        if (dto.getCo2() < 0) {
            throw new RuntimeException("El CO2 no puede ser negativo");
        }
    }
}
