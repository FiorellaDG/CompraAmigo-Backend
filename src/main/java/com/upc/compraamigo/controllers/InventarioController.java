package com.upc.compraamigo.controllers;

import com.upc.compraamigo.repositories.ProductoRepositorio;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/admin/products")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class InventarioController {
    private final ProductoRepositorio productos;
    public record Stock(@NotNull @PositiveOrZero Integer stock) {}

    @PatchMapping("/{id}/stock")
    @Transactional
    public Stock stock(@PathVariable Long id, @Valid @RequestBody Stock ajuste) {
        var producto = productos.bloquearPorId(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        producto.setStock(ajuste.stock());
        return ajuste;
    }
}
