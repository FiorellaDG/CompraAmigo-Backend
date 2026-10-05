package com.upc.compraamigo.controllers;

import com.upc.compraamigo.dtos.ImpactoCategoriaDTO;
import com.upc.compraamigo.dtos.ImpactoUsuarioDTO;
import com.upc.compraamigo.services.ImpactoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class ImpactoController {
    @Autowired
    private ImpactoService impactoService;

    @GetMapping("/impacto/usuario/{userId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ImpactoUsuarioDTO> impactoUsuario(@PathVariable Long userId) {
        return ResponseEntity.ok(impactoService.obtenerImpactoUsuario(userId));
    }

    @GetMapping("/impacto/categorias")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ImpactoCategoriaDTO>> impactoCategorias() {
        return ResponseEntity.ok(impactoService.obtenerImpactoPorCategoria());
    }
}
