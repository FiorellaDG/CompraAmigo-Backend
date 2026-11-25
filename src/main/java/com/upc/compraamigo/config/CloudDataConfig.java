package com.upc.compraamigo.config;

import com.upc.compraamigo.security.entities.Role;
import com.upc.compraamigo.security.entities.User;
import com.upc.compraamigo.security.repositories.RoleRepository;
import com.upc.compraamigo.security.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("cloud")
@RequiredArgsConstructor
public class CloudDataConfig implements CommandLineRunner {
    private final RoleRepository roles;
    private final UserRepository usuarios;
    private final PasswordEncoder passwords;

    @Value("${compraamigo.bootstrap.admin-email:}")
    private String adminEmail;
    @Value("${compraamigo.bootstrap.admin-password:}")
    private String adminPassword;

    @Override
    @Transactional
    public void run(String... args) {
        role("ROLE_USER");
        Role adminRole = role("ROLE_ADMIN");
        if (adminEmail.isBlank() && adminPassword.isBlank()) return;
        if (adminEmail.isBlank() || adminPassword.isBlank()) {
            throw new IllegalStateException("Configura APP_INIT_ADMIN_EMAIL y APP_INIT_ADMIN_PASSWORD juntos.");
        }
        String correo = adminEmail.trim();
        if (usuarios.findByUsername(correo).isPresent()) return;
        User admin = new User();
        admin.setNombre("Administrador CompraAmigo");
        admin.setUsername(correo);
        admin.setPassword(passwords.encode(adminPassword));
        admin.setRole(adminRole);
        usuarios.save(admin);
    }

    private Role role(String nombre) {
        return roles.findByName(nombre).orElseGet(() -> {
            Role rol = new Role();
            rol.setName(nombre);
            return roles.save(rol);
        });
    }
}
