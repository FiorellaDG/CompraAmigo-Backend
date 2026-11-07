package com.upc.compraamigo.repositories;

import com.upc.compraamigo.entities.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritoRepositorio extends JpaRepository<Favorito, Long> {
    List<Favorito> findByUserId(Long userId);
    boolean existsByUserIdAndProductoId(Long userId, Long productoId);
}