package com.upc.compraamigo.controllers;

import com.upc.compraamigo.dtos.ProductoDTO;
import com.upc.compraamigo.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @GetMapping("/productos")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<ProductoDTO>> listaProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax) {
        return ResponseEntity.ok(productoService.buscarProductos(nombre, categoriaId, precioMin, precioMax));
    }

    @GetMapping("/producto/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ProductoDTO> buscarProducto(@PathVariable long id) {
        return ResponseEntity.ok(productoService.buscarProductoPorId(id));
    }

    @PostMapping("/producto")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoDTO> adicionarProducto(@RequestBody ProductoDTO productoDTO) {
        return ResponseEntity.ok(productoService.insertarProducto(productoDTO));
    }

    @PutMapping("/producto")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoDTO> editarProducto(@RequestBody ProductoDTO productoDTO) {
        return ResponseEntity.ok(productoService.modificarProducto(productoDTO));
    }

    @DeleteMapping("/producto/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
    }
}
