package com.upc.compraamigo.serviceimpl;

import com.upc.compraamigo.repositories.DetallePedidoRepositorio;
import com.upc.compraamigo.repositories.PedidoRepositorio;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.stream.Collectors;
import static org.springframework.web.util.HtmlUtils.htmlEscape;

@Service
@Slf4j
public class CorreoPedidoService {
    public record PedidoConfirmado(Long id) {}

    private final PedidoRepositorio pedidos;
    private final DetallePedidoRepositorio detalles;
    private final JavaMailSender correo;
    private final String remitente;

    public CorreoPedidoService(PedidoRepositorio pedidos, DetallePedidoRepositorio detalles,
                               JavaMailSender correo, @Value("${compraamigo.mail.from}") String remitente) {
        this.pedidos = pedidos;
        this.detalles = detalles;
        this.correo = correo;
        this.remitente = remitente;
    }

    @Async
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void alConfirmar(PedidoConfirmado evento) {
        try {
            enviar(evento.id(), null);
        } catch (RuntimeException ex) {
            log.warn("No se pudo enviar el recibo del pedido {}. Puede reintentarse desde la API.", evento.id());
        }
    }

    @Transactional
    public void enviarAlCliente(Long pedidoId, String username) {
        enviar(pedidoId, username);
    }

    private void enviar(Long id, String username) {
        var pedido = pedidos.bloquearPorId(id)
                .filter(p -> p.getUser() != null && (username == null || username.equals(p.getUser().getUsername())))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
        if (pedido.getCorreoEnviadoEn() != null) return;

        String filas = detalles.findByPedidoPedidoId(id).stream()
                .map(d -> "<tr><td>" + htmlEscape(d.getProducto().getNombre()) + "</td><td>" +
                        d.getCantidad() + "</td><td>" + moneda(d.getPrecioUnitario() * d.getCantidad()) + "</td></tr>")
                .collect(Collectors.joining());
        String html = """
                <html lang="es"><body>
                <h1>CompraAmigo</h1><h2>Confirmación del pedido #%d</h2>
                <p>Hola, %s. Recibimos tu pedido.</p>
                <table cellpadding="8" border="1"><thead><tr><th>Producto</th><th>Cantidad</th><th>Importe</th></tr></thead>
                <tbody>%s</tbody></table>
                <p>Subtotal: %s · Envío: %s</p><p><strong>Total: %s</strong></p>
                <p>Estado: %s</p></body></html>
                """.formatted(id, htmlEscape(pedido.getNombreReceptor() == null ? "cliente" : pedido.getNombreReceptor()),
                filas, moneda(pedido.getSubtotal()), moneda(pedido.getCostoEnvio()), moneda(pedido.getTotal()),
                htmlEscape(pedido.getEstado()));
        try {
            var mensaje = correo.createMimeMessage();
            var ayuda = new MimeMessageHelper(mensaje, "UTF-8");
            ayuda.setFrom(remitente);
            ayuda.setTo(pedido.getUser().getUsername());
            ayuda.setSubject("CompraAmigo · Confirmación del pedido #" + id);
            ayuda.setText(html, true);
            correo.send(mensaje);
            pedido.setCorreoEnviadoEn(LocalDateTime.now());
        } catch (MailException | MessagingException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "No se pudo enviar el correo de confirmación", ex);
        }
    }

    private String moneda(double monto) {
        return String.format(Locale.US, "S/ %.2f", monto);
    }
}
