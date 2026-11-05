package com.upc.compraamigo.dtos;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroUsuarioDTO {
    private String nombre;
    private String username;
    private String password;
}
