package com.upc.compraamigo.serviceimpl;

import com.upc.compraamigo.dtos.ImpactoCategoriaDTO;
import com.upc.compraamigo.dtos.ImpactoUsuarioDTO;
import com.upc.compraamigo.repositories.DetallePedidoRepositorio;
import com.upc.compraamigo.repositories.PedidoRepositorio;
import com.upc.compraamigo.security.repositories.UserRepository;
import com.upc.compraamigo.services.ImpactoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImpactoServiceImpl implements ImpactoService {
    @Autowired
    private DetallePedidoRepositorio detallePedidoRepositorio;
    @Autowired
    private PedidoRepositorio pedidoRepositorio;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ImpactoUsuarioDTO obtenerImpactoUsuario(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + userId);
        }

        Object[] resumen = detallePedidoRepositorio.resumenImpactoUsuario(userId);
        long productos = 0;
        double co2 = 0;

        if (resumen != null && resumen.length >= 2) {
            if (resumen[0] instanceof Number numeroProductos) {
                productos = numeroProductos.longValue();
            }
            if (resumen[1] instanceof Number numeroCo2) {
                co2 = numeroCo2.doubleValue();
            }
        }

        return new ImpactoUsuarioDTO(
                userId,
                pedidoRepositorio.countByUserId(userId),
                productos,
                co2,
                co2 / 21.0
        );
    }

    @Override
    public List<ImpactoCategoriaDTO> obtenerImpactoPorCategoria() {
        List<ImpactoCategoriaDTO> resultado = new ArrayList<>();

        for (Object[] fila : detallePedidoRepositorio.resumenImpactoPorCategoria()) {
            String categoria = String.valueOf(fila[0]);
            long unidades = ((Number) fila[1]).longValue();
            double co2 = ((Number) fila[2]).doubleValue();

            String estado = co2 > 0 ? "Optimizado" : "Sin impacto registrado";
            resultado.add(new ImpactoCategoriaDTO(categoria, unidades, co2, estado));
        }

        return resultado;
    }
}
