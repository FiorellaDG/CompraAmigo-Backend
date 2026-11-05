package com.upc.compraamigo.serviceimpl;

import com.upc.compraamigo.dtos.CrearPedidoRequestDTO;
import com.upc.compraamigo.dtos.DetallePedidoDTO;
import com.upc.compraamigo.dtos.DetallePedidoResponseDTO;
import com.upc.compraamigo.dtos.PedidoDTO;
import com.upc.compraamigo.entities.DetallePedido;
import com.upc.compraamigo.entities.Pedido;
import com.upc.compraamigo.entities.Producto;
import com.upc.compraamigo.repositories.DetallePedidoRepositorio;
import com.upc.compraamigo.repositories.PedidoRepositorio;
import com.upc.compraamigo.repositories.ProductoRepositorio;
import com.upc.compraamigo.security.entities.User;
import com.upc.compraamigo.security.repositories.UserRepository;
import com.upc.compraamigo.services.PedidoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PedidoServiceImpl implements PedidoService {

    private static final Set<String> ESTADOS_VALIDOS = Set.of(
            "En preparación",
            "En camino",
            "Entregado"
    );

    @Autowired
    private PedidoRepositorio pedidoRepositorio;
    @Autowired
    private DetallePedidoRepositorio detallePedidoRepositorio;
    @Autowired
    private ProductoRepositorio productoRepositorio;
    @Autowired
    private UserRepository userRepository;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public boolean crearPedido(CrearPedidoRequestDTO requestDTO) {
        if (requestDTO.getDetalles() == null || requestDTO.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe contener al menos un producto");
        }

        User user = userRepository.findById(requestDTO.getUserID())
                .orElseThrow(() -> new IllegalArgumentException("User no encontrado con ID: " + requestDTO.getUserID()));

        Pedido pedido = new Pedido();
        pedido.setUser(user);
        pedido.setFecha(LocalDate.now());
        pedido.setEstado("En preparación");
        pedido.setNombreReceptor(requestDTO.getNombreReceptor());
        pedido.setTelefono(requestDTO.getTelefono());
        pedido.setCiudad(requestDTO.getCiudad());
        pedido.setDistrito(requestDTO.getDistrito());
        pedido.setDireccionEnvio(requestDTO.getDireccionEnvio());
        pedido.setMetodoEnvio(normalizarMetodoEnvio(requestDTO.getMetodoEnvio()));
        pedido.setMetodoPago(requestDTO.getMetodoPago());

        List<DetallePedido> detallesPreparados = new ArrayList<>();
        double subtotal = 0;
        double co2Ahorrado = 0;

        for (DetallePedidoDTO detalleDTO : requestDTO.getDetalles()) {
            if (detalleDTO.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
            }

            Producto producto = productoRepositorio.findById(detalleDTO.getProducto().getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Producto no encontrado con ID: " + detalleDTO.getProducto().getId()));

            if (producto.getStock() < detalleDTO.getCantidad()) {
                throw new IllegalArgumentException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - detalleDTO.getCantidad());
            productoRepositorio.save(producto);

            DetallePedido detalle = new DetallePedido();
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setTalla(normalizarTalla(detalleDTO.getTalla()));
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setCo2Unitario(producto.getCo2());
            detallesPreparados.add(detalle);

            subtotal += producto.getPrecio() * detalleDTO.getCantidad();
            co2Ahorrado += producto.getCo2() * detalleDTO.getCantidad();
        }

        double costoEnvio = calcularCostoEnvio(pedido.getMetodoEnvio(), subtotal);
        pedido.setSubtotal(subtotal);
        pedido.setCostoEnvio(costoEnvio);
        pedido.setTotal(subtotal + costoEnvio);
        pedido.setCo2Ahorrado(co2Ahorrado);

        Pedido pedidoGuardado = pedidoRepositorio.save(pedido);

        for (DetallePedido detalle : detallesPreparados) {
            detalle.setPedido(pedidoGuardado);
            detallePedidoRepositorio.save(detalle);
        }

        return true;
    }

    @Override
    public List<PedidoDTO> obtenerTodosLosPedidos() {
        return pedidoRepositorio.findAll().stream()
                .map(this::convertirDTO)
                .toList();
    }

    @Override
    public PedidoDTO obtenerPedidoPorId(Long pedidoId) {
        Pedido pedido = pedidoRepositorio.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + pedidoId));
        return convertirDTO(pedido);
    }

    @Override
    public List<PedidoDTO> obtenerPedidosPorUsuario(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("Usuario no encontrado con ID: " + userId);
        }
        return pedidoRepositorio.findByUserIdOrderByFechaDesc(userId).stream()
                .map(this::convertirDTO)
                .toList();
    }

    @Transactional
    @Override
    public PedidoDTO actualizarEstado(Long pedidoId, String estado) {
        if (!ESTADOS_VALIDOS.contains(estado)) {
            throw new IllegalArgumentException(
                    "Estado inválido. Use: En preparación, En camino o Entregado"
            );
        }

        Pedido pedido = pedidoRepositorio.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + pedidoId));
        pedido.setEstado(estado);
        return convertirDTO(pedidoRepositorio.save(pedido));
    }

    private PedidoDTO convertirDTO(Pedido pedido) {
        PedidoDTO dto = new PedidoDTO();
        dto.setPedidoId(pedido.getPedidoId());
        dto.setFecha(pedido.getFecha());
        dto.setEstado(pedido.getEstado());
        dto.setUserID(pedido.getUser() != null ? pedido.getUser().getId() : null);
        dto.setNombreUsuario(pedido.getUser() != null ? pedido.getUser().getNombre() : null);
        dto.setNombreReceptor(pedido.getNombreReceptor());
        dto.setTelefono(pedido.getTelefono());
        dto.setCiudad(pedido.getCiudad());
        dto.setDistrito(pedido.getDistrito());
        dto.setDireccionEnvio(pedido.getDireccionEnvio());
        dto.setMetodoEnvio(pedido.getMetodoEnvio());
        dto.setCostoEnvio(pedido.getCostoEnvio());
        dto.setMetodoPago(pedido.getMetodoPago());
        dto.setSubtotal(pedido.getSubtotal());
        dto.setTotal(pedido.getTotal());
        dto.setCo2Ahorrado(pedido.getCo2Ahorrado());

        List<DetallePedidoResponseDTO> detalles = detallePedidoRepositorio
                .findByPedidoPedidoId(pedido.getPedidoId())
                .stream()
                .map(this::convertirDetalleDTO)
                .toList();
        dto.setDetalles(detalles);
        return dto;
    }

    private DetallePedidoResponseDTO convertirDetalleDTO(DetallePedido detalle) {
        DetallePedidoResponseDTO dto = new DetallePedidoResponseDTO();
        dto.setDetalleId(detalle.getDetalleId());
        dto.setProductoID(detalle.getProducto().getId());
        dto.setNombreProducto(detalle.getProducto().getNombre());
        dto.setCantidad(detalle.getCantidad());
        dto.setTalla(detalle.getTalla());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setCo2Unitario(detalle.getCo2Unitario());
        return dto;
    }

    private String normalizarMetodoEnvio(String metodoEnvio) {
        if (metodoEnvio == null || metodoEnvio.isBlank()) {
            return "Estándar";
        }
        return metodoEnvio.trim();
    }

    private String normalizarTalla(String talla) {
        if (talla == null || talla.isBlank()) {
            return "UNICA";
        }
        return talla.trim().toUpperCase();
    }

    private double calcularCostoEnvio(String metodoEnvio, double subtotal) {
        if ("Recojo".equalsIgnoreCase(metodoEnvio)) {
            return 0;
        }
        if ("Express".equalsIgnoreCase(metodoEnvio)) {
            return 20;
        }
        if (subtotal >= 200) {
            return 0;
        }
        return 10;
    }
}
