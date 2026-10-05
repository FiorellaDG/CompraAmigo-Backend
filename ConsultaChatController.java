package com.upc.compraamigo.controllers;

import com.upc.compraamigo.dtos.ConsultaChatDTO;
import com.upc.compraamigo.services.ConsultaChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class ConsultaChatController {
    @Autowired
    private ConsultaChatService consultaChatService;

    @PostMapping("/chat")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ConsultaChatDTO> registrarConsulta(@RequestBody ConsultaChatDTO consultaChatDTO) {
        return ResponseEntity.ok(consultaChatService.registrarConsulta(consultaChatDTO));
    }

    @GetMapping("/chat/consultas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ConsultaChatDTO>> listarConsultas() {
        return ResponseEntity.ok(consultaChatService.listarConsultas());
    }
}
