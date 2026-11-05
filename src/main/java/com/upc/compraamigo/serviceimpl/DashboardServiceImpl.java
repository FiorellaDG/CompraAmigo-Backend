package com.upc.compraamigo.serviceimpl;

import com.upc.compraamigo.dtos.DashboardDTO;
import com.upc.compraamigo.repositories.ConsultaChatRepositorio;
import com.upc.compraamigo.repositories.PedidoRepositorio;
import com.upc.compraamigo.repositories.ProductoRepositorio;
import com.upc.compraamigo.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private PedidoRepositorio pedidoRepositorio;
    @Autowired
    private ProductoRepositorio productoRepositorio;
    @Autowired
    private ConsultaChatRepositorio consultaChatRepositorio;

    @Override
    public DashboardDTO obtenerResumen() {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioMes = hoy.withDayOfMonth(1);
        LocalDate finMes = hoy.withDayOfMonth(hoy.lengthOfMonth());

        Double ventasDia = pedidoRepositorio.sumarVentasPorFecha(hoy);
        Double ventasMes = pedidoRepositorio.sumarVentasEntre(inicioMes, finMes);

        return new DashboardDTO(
                ventasDia != null ? ventasDia : 0,
                ventasMes != null ? ventasMes : 0,
                pedidoRepositorio.count(),
                pedidoRepositorio.countByEstado("En preparación"),
                productoRepositorio.count(),
                consultaChatRepositorio.count()
        );
    }
}
