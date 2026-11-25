package com.upc.compraamigo.serviceimpl;

import com.upc.compraamigo.dtos.ConsultaChatDTO;
import com.upc.compraamigo.entities.Pedido;
import com.upc.compraamigo.entities.TicketSoporte;
import com.upc.compraamigo.repositories.PedidoRepositorio;
import com.upc.compraamigo.repositories.TicketSoporteRepositorio;
import com.upc.compraamigo.security.entities.User;
import com.upc.compraamigo.security.repositories.UserRepository;
import com.upc.compraamigo.services.CarritoService;
import com.upc.compraamigo.services.ConsultaChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.Normalizer;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ExperienciaClienteService {
    private final UserRepository usuarios;
    private final PedidoRepositorio pedidos;
    private final CarritoService carritos;
    private final ConsultaChatService consultas;
    private final TicketSoporteRepositorio tickets;

    public record Impacto(double kgCo2Ahorrados, int unidades) {}
    public record RespuestaChat(String respuesta, boolean ofrecerSoporte) {}
    public record Seguimiento(Long id, String codigo) {}

    @Transactional(readOnly = true)
    public Impacto impacto(String username) {
        var items = carritos.listarCarrito(cliente(username).getId());
        double co2 = items.stream().mapToDouble(item -> item.getCo2() * item.getCantidad()).sum();
        int unidades = items.stream().mapToInt(item -> item.getCantidad()).sum();
        return new Impacto(co2, unidades);
    }

    @Transactional
    public RespuestaChat responder(String username, String mensaje, Long pedidoId) {
        User cliente = cliente(username);
        String texto = Normalizer.normalize(mensaje.toLowerCase(Locale.ROOT), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").replaceAll("\\s+", " ");
        RespuestaChat respuesta;
        if (texto.matches(".*\\b(pago|pagos|pagar|yape|plin|tarjeta)\\b.*")) {
            respuesta = new RespuestaChat("Los medios de pago disponibles en el checkout son contra entrega y Yape / Plin.", false);
        } else if (texto.matches(".*\\b(pedido|pedidos|orden|estado|seguimiento)\\b.*")) {
            if (pedidoId == null) {
                respuesta = new RespuestaChat("Indica el número del pedido o consulta su estado en Mis pedidos.", false);
            } else {
                Pedido pedido = pedidoDelCliente(pedidoId, cliente.getId());
                respuesta = new RespuestaChat("Tu pedido #" + pedidoId + " está en estado: " + pedido.getEstado() + ".", false);
            }
        } else if (texto.matches(".*\\b(envio|envios|entrega|entregas|recojo|delivery)\\b.*")) {
            respuesta = new RespuestaChat("En el checkout puedes elegir envío estándar, exprés o recojo en tienda.", false);
        } else {
            respuesta = new RespuestaChat("Puedo ayudarte con pedidos, envíos y pagos. También puedes registrar un ticket de soporte.", true);
        }
        var consulta = new ConsultaChatDTO();
        consulta.setUserID(cliente.getId());
        consulta.setConsulta(mensaje);
        consulta.setRespuesta(respuesta.respuesta());
        consulta.setResultado(respuesta.ofrecerSoporte() ? "SOPORTE" : "RESPONDIDA");
        consultas.registrarConsulta(consulta);
        return respuesta;
    }

    @Transactional
    public Seguimiento registrar(String username, String asunto, String mensaje, Long pedidoId) {
        User cliente = cliente(username);
        Pedido pedido = pedidoId == null ? null : pedidoDelCliente(pedidoId, cliente.getId());
        TicketSoporte ticket = tickets.save(new TicketSoporte(cliente, pedido, asunto.trim(), mensaje.trim()));
        return new Seguimiento(ticket.getId(), "CA-" + ticket.getId());
    }

    private User cliente(String username) {
        return usuarios.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

    private Pedido pedidoDelCliente(Long pedidoId, Long userId) {
        return pedidos.findById(pedidoId)
                .filter(pedido -> pedido.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
    }
}
