package com.upc.compraamigo.controllers;

import com.upc.compraamigo.dtos.CrearPedidoRequestDTO;
import com.upc.compraamigo.dtos.PedidoDTO;
import com.upc.compraamigo.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/pedido")
    @PreAuthorize("hasRole('USER')")
    public boolean crearPedido(@RequestBody CrearPedidoRequestDTO crearPedidoRequestDTO) {
        return pedidoService.crearPedido(crearPedidoRequestDTO);
    }

    @GetMapping("/pedidos")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PedidoDTO> obtenerTodosLosPedidos() {
        return pedidoService.obtenerTodosLosPedidos();
    }

    @GetMapping("/pedidos/usuario/{userId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<PedidoDTO> obtenerPedidosPorUsuario(@PathVariable Long userId) {
        return pedidoService.obtenerPedidosPorUsuario(userId);
    }

    @GetMapping("/pedido/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PedidoDTO> obtenerPedidoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPedidoPorId(id));
    }

    @PutMapping("/pedido/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PedidoDTO> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, estado));
    }
}
