package com.upc.compraamigo.repositories;

import com.upc.compraamigo.entities.Pedido;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PedidoRepositorio extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUserId(Long userId);
    List<Pedido> findByUserIdOrderByFechaDesc(Long userId);
    long countByUserId(Long userId);
    long countByEstado(String estado);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Pedido p WHERE p.pedidoId = :id")
    Optional<Pedido> bloquearPorId(@Param("id") Long id);

    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p WHERE p.fecha = :fecha")
    Double sumarVentasPorFecha(@Param("fecha") LocalDate fecha);

    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p WHERE p.fecha BETWEEN :inicio AND :fin")
    Double sumarVentasEntre(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);
}
