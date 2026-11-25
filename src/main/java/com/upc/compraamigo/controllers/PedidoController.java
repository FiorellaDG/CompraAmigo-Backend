package com.upc.compraamigo.controllers;

import com.upc.compraamigo.dtos.CrearPedidoRequestDTO;
import com.upc.compraamigo.dtos.PedidoDTO;
import com.upc.compraamigo.services.PedidoService;
import com.upc.compraamigo.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.security.Principal;

@RestController
@CrossOrigin(origins = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private UserRepository usuarios;

    @PostMapping("/pedido")
    @PreAuthorize("hasRole('USER')")
    public boolean crearPedido(@RequestBody CrearPedidoRequestDTO crearPedidoRequestDTO, Principal principal) {
        if (usuarios.findByUsername(principal.getName())
                .filter(usuario -> usuario.getId().equals(crearPedidoRequestDTO.getUserID())).isEmpty()) {
            throw new AccessDeniedException("El pedido debe pertenecer al cliente autenticado");
        }
        return pedidoService.crearPedido(crearPedidoRequestDTO);
    }

    @GetMapping("/pedidos")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PedidoDTO> obtenerTodosLosPedidos() {
        return pedidoService.obtenerTodosLosPedidos();
    }

    @GetMapping("/pedidos/usuario/{userId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<PedidoDTO> obtenerPedidosPorUsuario(@PathVariable Long userId, Authentication sesion) {
        verificarPropietario(userId, sesion);
        return pedidoService.obtenerPedidosPorUsuario(userId);
    }

    @GetMapping("/pedido/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PedidoDTO> obtenerPedidoPorId(@PathVariable Long id, Authentication sesion) {
        PedidoDTO pedido = pedidoService.obtenerPedidoPorId(id);
        verificarPropietario(pedido.getUserID(), sesion);
        return ResponseEntity.ok(pedido);
    }

    private void verificarPropietario(Long userId, Authentication sesion) {
        boolean admin = sesion.getAuthorities().stream().anyMatch(rol -> rol.getAuthority().equals("ROLE_ADMIN"));
        if (!admin && usuarios.findByUsername(sesion.getName())
                .filter(usuario -> usuario.getId().equals(userId)).isEmpty()) {
            throw new AccessDeniedException("No tienes acceso a este pedido");
        }
    }

    @PutMapping("/pedido/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PedidoDTO> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, estado));
    }
}
