package com.upc.compraamigo.repositories;

import com.upc.compraamigo.entities.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetallePedidoRepositorio extends JpaRepository<DetallePedido, Long> {

    List<DetallePedido> findByPedidoPedidoId(Long pedidoId);

    @Query("SELECT COALESCE(SUM(d.cantidad), 0), COALESCE(SUM(d.cantidad * d.co2Unitario), 0) " +
            "FROM DetallePedido d WHERE d.pedido.user.id = :userId")
    Object[] resumenImpactoUsuario(@Param("userId") Long userId);

    @Query("SELECT d.producto.categoria.nombre, COALESCE(SUM(d.cantidad), 0), " +
            "COALESCE(SUM(d.cantidad * d.co2Unitario), 0) " +
            "FROM DetallePedido d GROUP BY d.producto.categoria.nombre ORDER BY d.producto.categoria.nombre")
    List<Object[]> resumenImpactoPorCategoria();
}
