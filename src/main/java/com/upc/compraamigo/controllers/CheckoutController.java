package com.upc.compraamigo.controllers;

import com.upc.compraamigo.dtos.CrearPedidoRequestDTO;
import com.upc.compraamigo.dtos.PedidoDTO;
import com.upc.compraamigo.security.repositories.UserRepository;
import com.upc.compraamigo.services.PedidoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/checkout")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class CheckoutController {
    private final PedidoService pedidos;
    private final UserRepository usuarios;

    public record Entrega(@NotBlank @Size(max = 100) String nombreReceptor,
                          @NotBlank @Pattern(regexp = "[0-9]{9}") String telefono,
                          @NotBlank @Size(max = 100) String ciudad,
                          @NotBlank @Size(max = 100) String distrito,
                          @NotBlank @Size(max = 250) String direccionEnvio,
                          @Pattern(regexp = "estandar|express|recojo") @NotBlank String metodoEnvio,
                          @Pattern(regexp = "Contra entrega|Yape / Plin") @NotBlank String metodoPago) {}

    @GetMapping("/total")
    public PedidoService.TotalCheckout total(Principal principal, @RequestParam(defaultValue = "estandar") String envio) {
        return pedidos.cotizarCarrito(cliente(principal), envio);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoDTO confirmar(Principal principal, @Valid @RequestBody Entrega entrega) {
        var pedido = new CrearPedidoRequestDTO();
        pedido.setUserID(cliente(principal));
        pedido.setNombreReceptor(entrega.nombreReceptor().trim());
        pedido.setTelefono(entrega.telefono());
        pedido.setCiudad(entrega.ciudad().trim());
        pedido.setDistrito(entrega.distrito().trim());
        pedido.setDireccionEnvio(entrega.direccionEnvio().trim());
        pedido.setMetodoEnvio(entrega.metodoEnvio());
        pedido.setMetodoPago(entrega.metodoPago());
        return pedidos.confirmarCarrito(pedido);
    }

    private Long cliente(Principal principal) {
        return usuarios.findByUsername(principal.getName()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED)).getId();
    }
}
