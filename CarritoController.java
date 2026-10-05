package com.upc.compraamigo.controllers;

import com.upc.compraamigo.dtos.CarritoItemDTO;
import com.upc.compraamigo.services.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    // AGREGAR PRODUCTO

    @PostMapping("/carrito/item")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CarritoItemDTO>
    agregarProducto(
            @RequestBody CarritoItemDTO dto) {

        return ResponseEntity.ok(
                carritoService
                        .agregarProducto(dto)
        );
    }


    // CONSULTAR CARRITO

    @GetMapping(
            "/carrito/usuario/{userId}"
    )
    @PreAuthorize(
            "hasAnyRole('USER','ADMIN')"
    )
    public ResponseEntity<
            List<CarritoItemDTO>>
    listarCarrito(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                carritoService
                        .listarCarrito(userId)
        );
    }


    // CAMBIAR CANTIDAD

    @PutMapping(
            "/carrito/item/{id}"
    )
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CarritoItemDTO>
    modificarCantidad(

            @PathVariable Long id,

            @RequestParam Integer cantidad) {

        return ResponseEntity.ok(
                carritoService
                        .modificarCantidad(
                                id,
                                cantidad
                        )
        );
    }


    // ELIMINAR PRODUCTO

    @DeleteMapping(
            "/carrito/item/{id}"
    )
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void>
    eliminarProducto(
            @PathVariable Long id) {

        carritoService
                .eliminarProducto(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}