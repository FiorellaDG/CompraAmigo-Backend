package com.upc.compraamigo.repositories;

import com.upc.compraamigo.entities.DetalleCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetalleCarritoRepositorio extends JpaRepository<DetalleCarrito, Long> {
    List<DetalleCarrito> findByCarritoId(Long carritoId);

    Optional<DetalleCarrito> findByCarritoIdAndProductoIdAndTalla(
            Long carritoId,
            Long productoId,
            String talla
    );

    void deleteByCarritoId(Long carritoId);
}
