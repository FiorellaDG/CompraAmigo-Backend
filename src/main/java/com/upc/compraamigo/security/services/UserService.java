package com.upc.compraamigo.security.services;

import com.upc.compraamigo.security.entities.Role;
import com.upc.compraamigo.security.entities.User;
import com.upc.compraamigo.security.repositories.RoleRepository;
import com.upc.compraamigo.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void grabar(Role role) {
        roleRepository.save(role);
    }

    public Role buscarRol(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
    }

    public Role buscarRolPorNombre(String nombre) {
        return roleRepository.findByName(nombre)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + nombre));
    }

    public User buscarPorUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
    }

    public boolean existeUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
