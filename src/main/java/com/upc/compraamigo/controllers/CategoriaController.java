package com.upc.compraamigo.controllers;

import com.upc.compraamigo.dtos.CategoriaDTO;
import com.upc.compraamigo.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/categorias")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<CategoriaDTO>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.listarCategorias());
    }

    @PostMapping("/categoria")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaDTO> adicionarCategoria(@RequestBody CategoriaDTO categoriaDTO) {
        return ResponseEntity.ok(categoriaService.insertarCategoria(categoriaDTO));
    }
}
