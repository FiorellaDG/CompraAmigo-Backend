package com.upc.compraamigo.repositories;

import com.upc.compraamigo.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CategoriaRepositorio extends JpaRepository<Categoria, Long> {
}
