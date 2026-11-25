package com.upc.compraamigo.controllers;

import com.upc.compraamigo.serviceimpl.CorreoPedidoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificacionController {
    private final CorreoPedidoService correo;

    public record Solicitud(@NotNull @Positive Long pedidoId) {}

    @PostMapping("/email-receipt")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('USER')")
    public void enviar(@Valid @RequestBody Solicitud solicitud, Principal principal) {
        correo.enviarAlCliente(solicitud.pedidoId(), principal.getName());
    }
}
