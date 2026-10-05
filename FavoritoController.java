package com.upc.compraamigo.controllers;

import com.upc.compraamigo.dtos.FavoritoDTO;
import com.upc.compraamigo.services.FavoritoService;
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
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    @PostMapping("/favorito")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<FavoritoDTO> agregarFavorito(
            @RequestBody FavoritoDTO favoritoDTO) {

        return ResponseEntity.ok(
                favoritoService.agregarFavorito(favoritoDTO)
        );
    }

    @GetMapping("/favoritos/usuario/{userId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<FavoritoDTO>> listarFavoritos(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                favoritoService.listarFavoritosPorUsuario(userId)
        );
    }

    @DeleteMapping("/favorito/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> eliminarFavorito(
            @PathVariable Long id) {

        favoritoService.eliminarFavorito(id);

        return ResponseEntity.noContent().build();
    }
}
