package com.upc.compraamigo.controllers;

import com.upc.compraamigo.dtos.CarritoItemDTO;
import com.upc.compraamigo.repositories.DetalleCarritoRepositorio;
import com.upc.compraamigo.security.repositories.UserRepository;
import com.upc.compraamigo.services.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.security.Principal;

@RestController
@CrossOrigin(
        origins = "${ip.frontend}",
        allowCredentials = "true",
        exposedHeaders = "Authorization"
)
@RequestMapping("/api")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;
    @Autowired
    private UserRepository usuarios;
    @Autowired
    private DetalleCarritoRepositorio detalles;


    // Agregar producto

    @PostMapping("/carrito/item")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CarritoItemDTO>
    agregarProducto(
            @RequestBody CarritoItemDTO dto, Principal sesion) {
        if (!cliente(sesion).equals(dto.getUserID())) {
            throw new AccessDeniedException("El carrito debe pertenecer al cliente autenticado");
        }

        return ResponseEntity.ok(
                carritoService
                        .agregarProducto(dto)
        );
    }


    // Consultar carrito

    @GetMapping("/carrito/usuario/{userId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<
            List<CarritoItemDTO>>
    listarCarrito(
            @PathVariable Long userId, Authentication sesion) {
        boolean admin = sesion.getAuthorities().stream().anyMatch(rol -> rol.getAuthority().equals("ROLE_ADMIN"));
        if (!admin && !cliente(sesion).equals(userId)) {
            throw new AccessDeniedException("No tienes acceso a este carrito");
        }

        return ResponseEntity.ok(
                carritoService
                        .listarCarrito(userId)
        );
    }


    // Cambiar cantidad
    @PutMapping("/carrito/item/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CarritoItemDTO>
    modificarCantidad(

            @PathVariable Long id,
            @RequestParam Integer cantidad, Principal sesion) {
        verificarLinea(id, sesion);

        return ResponseEntity.ok(
                carritoService
                        .modificarCantidad(
                                id,
                                cantidad
                        )
        );
    }


    // eliminar producto

    @DeleteMapping("/carrito/item/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void>
    eliminarProducto(
            @PathVariable Long id, Principal sesion) {
        verificarLinea(id, sesion);
        carritoService
                .eliminarProducto(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    private void verificarLinea(Long id, Principal sesion) {
        if (!detalles.existsByIdAndCarritoUserId(id, cliente(sesion))) {
            throw new AccessDeniedException("No tienes acceso a este artículo del carrito");
        }
    }

    private Long cliente(Principal sesion) {
        return usuarios.findByUsername(sesion.getName())
                .orElseThrow(() -> new AccessDeniedException("Cliente no encontrado")).getId();
    }
}
