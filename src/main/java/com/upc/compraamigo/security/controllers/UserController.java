package com.upc.compraamigo.security.controllers;

import com.upc.compraamigo.dtos.RegistroUsuarioDTO;
import com.upc.compraamigo.security.entities.Role;
import com.upc.compraamigo.security.entities.User;
import com.upc.compraamigo.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder bcrypt;

    @PostMapping("/register")
    public ResponseEntity<String> registrarCliente(@RequestBody RegistroUsuarioDTO registroDTO) {
        if (userService.existeUsername(registroDTO.getUsername())) {
            throw new RuntimeException("El correo ya se encuentra registrado");
        }
        Role roleUser = userService.buscarRolPorNombre("ROLE_USER");

        User user = new User();
        user.setNombre(registroDTO.getNombre());
        user.setUsername(registroDTO.getUsername());
        user.setPassword(bcrypt.encode(registroDTO.getPassword()));
        user.setRole(roleUser);
        userService.save(user);

        return ResponseEntity.ok("Usuario registrado correctamente");
    }

    @PostMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
    public void createUser(@RequestBody User user) {
        user.setPassword(bcrypt.encode(user.getPassword()));
        userService.save(user);
    }

    @PostMapping("/rol")
    @PreAuthorize("hasRole('ADMIN')")
    public void createRol(@RequestBody Role rol) {
        userService.grabar(rol);
    }
}
