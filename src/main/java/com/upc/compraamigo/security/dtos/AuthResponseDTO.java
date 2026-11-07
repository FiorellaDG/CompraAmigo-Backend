package com.upc.compraamigo.security.dtos;

import lombok.Data;

import java.util.Set;

@Data

public class AuthResponseDTO {
    private String jwt;
    private Long userId;
    private String nombre;
    private String username;
    private Set<String> roles;
}
