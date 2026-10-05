package com.upc.compraamigo.controllers;

import com.upc.compraamigo.dtos.DashboardDTO;
import com.upc.compraamigo.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard/resumen")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardDTO> resumen() {
        return ResponseEntity.ok(dashboardService.obtenerResumen());
    }
}
