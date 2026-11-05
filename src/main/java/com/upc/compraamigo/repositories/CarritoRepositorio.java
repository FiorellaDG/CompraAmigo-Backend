package com.upc.compraamigo.repositories;

import com.upc.compraamigo.entities.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepositorio
        extends JpaRepository<Carrito, Long> {

    Optional<Carrito> findByUserId(Long userId);
}