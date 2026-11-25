package com.upc.compraamigo.controllers;

import com.upc.compraamigo.serviceimpl.ExperienciaClienteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class ExperienciaClienteController {
    private final ExperienciaClienteService experiencia;

    public record Consulta(@NotBlank @Size(max = 250) String mensaje, @Positive Long pedidoId) {}
    public record Ticket(@NotBlank @Size(max = 120) String asunto,
                         @NotBlank @Size(max = 2000) String mensaje, @Positive Long pedidoId) {}

    @GetMapping("/cart/eco-impact")
    public ExperienciaClienteService.Impacto impacto(Principal principal) {
        return experiencia.impacto(principal.getName());
    }

    @PostMapping("/bot/chat")
    public ExperienciaClienteService.RespuestaChat chat(Principal principal, @Valid @RequestBody Consulta consulta) {
        return experiencia.responder(principal.getName(), consulta.mensaje().trim(), consulta.pedidoId());
    }

    @PostMapping("/support/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    public ExperienciaClienteService.Seguimiento ticket(Principal principal, @Valid @RequestBody Ticket ticket) {
        return experiencia.registrar(principal.getName(), ticket.asunto(), ticket.mensaje(), ticket.pedidoId());
    }
}
