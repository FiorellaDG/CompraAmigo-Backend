package com.upc.compraamigo.security.controllers;

import com.upc.compraamigo.security.entities.User;
import com.upc.compraamigo.security.repositories.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/profile")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class PerfilController {
    private final UserRepository usuarios;

    public record Perfil(String nombre, String correo, String telefono, String ciudad, String distrito, String direccion) {}
    public record Cambios(@NotBlank @Size(max = 100) String nombre,
                          @NotBlank @Pattern(regexp = "[0-9]{9}") String telefono,
                          @NotBlank @Size(max = 100) String ciudad,
                          @NotBlank @Size(max = 100) String distrito,
                          @NotBlank @Size(max = 250) String direccion) {}

    @GetMapping
    public Perfil obtener(Principal principal) {
        return perfil(usuario(principal));
    }

    @PutMapping
    public Perfil guardar(Principal principal, @Valid @RequestBody Cambios cambios) {
        User cliente = usuario(principal);
        cliente.setNombre(cambios.nombre().trim());
        cliente.setTelefonoPerfil(cambios.telefono());
        cliente.setCiudadPerfil(cambios.ciudad().trim());
        cliente.setDistritoPerfil(cambios.distrito().trim());
        cliente.setDireccionPerfil(cambios.direccion().trim());
        return perfil(usuarios.save(cliente));
    }

    private User usuario(Principal principal) {
        return usuarios.findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

    private Perfil perfil(User usuario) {
        return new Perfil(usuario.getNombre(), usuario.getUsername(), usuario.getTelefonoPerfil(),
                usuario.getCiudadPerfil(), usuario.getDistritoPerfil(), usuario.getDireccionPerfil());
    }
}
