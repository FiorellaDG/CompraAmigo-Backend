package com.upc.compraamigo.repositories;

import com.upc.compraamigo.entities.DetalleCarrito;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetalleCarritoRepositorio extends JpaRepository<DetalleCarrito, Long> {
    List<DetalleCarrito> findByCarritoId(Long carritoId);
    boolean existsByIdAndCarritoUserId(Long id, Long userId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT d FROM DetalleCarrito d WHERE d.carrito.id = :carritoId ORDER BY d.id")
    List<DetalleCarrito> bloquearLineas(@Param("carritoId") Long carritoId);
    Optional<DetalleCarrito> findByCarritoIdAndProductoIdAndTalla(
            Long carritoId,
            Long productoId,
            String talla
    );

    void deleteByCarritoId(Long carritoId);
}
