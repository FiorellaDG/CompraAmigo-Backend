package com.upc.compraamigo.serviceimpl;

import com.upc.compraamigo.dtos.CarritoItemDTO;
import com.upc.compraamigo.entities.Carrito;
import com.upc.compraamigo.entities.DetalleCarrito;
import com.upc.compraamigo.entities.Producto;
import com.upc.compraamigo.repositories.CarritoRepositorio;
import com.upc.compraamigo.repositories.DetalleCarritoRepositorio;
import com.upc.compraamigo.repositories.ProductoRepositorio;
import com.upc.compraamigo.security.entities.User;
import com.upc.compraamigo.security.repositories.UserRepository;
import com.upc.compraamigo.services.CarritoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarritoServiceImpl implements CarritoService {
    @Autowired
    private CarritoRepositorio carritoRepositorio;
    @Autowired
    private DetalleCarritoRepositorio detalleCarritoRepositorio;
    @Autowired
    private ProductoRepositorio productoRepositorio;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public CarritoItemDTO agregarProducto(CarritoItemDTO dto) {
        if (dto.getCantidad() == null || dto.getCantidad() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }

        User user = userRepository.findById(dto.getUserID())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getUserID()));

        Producto producto = productoRepositorio.findById(dto.getProductoID())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + dto.getProductoID()));

        if (dto.getCantidad() > producto.getStock()) {
            throw new RuntimeException("La cantidad supera el stock disponible");
        }

        Carrito carrito = carritoRepositorio.findByUserId(user.getId())
                .orElseGet(() -> {
                    Carrito nuevoCarrito = new Carrito();
                    nuevoCarrito.setUser(user);
                    return carritoRepositorio.save(nuevoCarrito);
                });

        String talla = normalizarTalla(dto.getTalla());

        Optional<DetalleCarrito> existente = detalleCarritoRepositorio
                .findByCarritoIdAndProductoIdAndTalla(carrito.getId(), producto.getId(), talla);

        DetalleCarrito detalle;
        if (existente.isPresent()) {
            detalle = existente.get();
            int nuevaCantidad = detalle.getCantidad() + dto.getCantidad();
            if (nuevaCantidad > producto.getStock()) {
                throw new RuntimeException("La cantidad supera el stock disponible");
            }
            detalle.setCantidad(nuevaCantidad);
        } else {
            detalle = new DetalleCarrito();
            detalle.setCarrito(carrito);
            detalle.setProducto(producto);
            detalle.setCantidad(dto.getCantidad());
            detalle.setTalla(talla);
        }

        return convertirDTO(detalleCarritoRepositorio.save(detalle));
    }

    @Override
    public List<CarritoItemDTO> listarCarrito(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + userId);
        }

        Optional<Carrito> carrito = carritoRepositorio.findByUserId(userId);
        if (carrito.isEmpty()) {
            return List.of();
        }

        return detalleCarritoRepositorio.findByCarritoId(carrito.get().getId())
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    @Transactional
    @Override
    public CarritoItemDTO modificarCantidad(Long detalleId, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }

        DetalleCarrito detalle = detalleCarritoRepositorio.findById(detalleId)
                .orElseThrow(() -> new RuntimeException("Producto del carrito no encontrado"));

        if (cantidad > detalle.getProducto().getStock()) {
            throw new RuntimeException("La cantidad supera el stock disponible");
        }

        detalle.setCantidad(cantidad);
        return convertirDTO(detalleCarritoRepositorio.save(detalle));
    }

    @Transactional
    @Override
    public void eliminarProducto(Long detalleId) {
        if (!detalleCarritoRepositorio.existsById(detalleId)) {
            throw new RuntimeException("Producto del carrito no encontrado");
        }
        detalleCarritoRepositorio.deleteById(detalleId);
    }

    private String normalizarTalla(String talla) {
        if (talla == null || talla.isBlank()) {
            return "UNICA";
        }
        return talla.trim().toUpperCase();
    }

    private CarritoItemDTO convertirDTO(DetalleCarrito detalle) {
        CarritoItemDTO dto = new CarritoItemDTO();
        dto.setId(detalle.getId());
        dto.setCarritoID(detalle.getCarrito().getId());
        dto.setUserID(detalle.getCarrito().getUser().getId());
        dto.setProductoID(detalle.getProducto().getId());
        dto.setNombreProducto(detalle.getProducto().getNombre());
        dto.setPrecio(detalle.getProducto().getPrecio());
        dto.setCantidad(detalle.getCantidad());
        dto.setTalla(detalle.getTalla());
        dto.setSubtotal(detalle.getProducto().getPrecio() * detalle.getCantidad());
        dto.setCo2(detalle.getProducto().getCo2());
        return dto;
    }
}
