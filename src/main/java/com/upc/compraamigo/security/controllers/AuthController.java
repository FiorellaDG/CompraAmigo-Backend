package com.upc.compraamigo.security.controllers;

import com.upc.compraamigo.security.dtos.AuthRequestDTO;
import com.upc.compraamigo.security.dtos.AuthResponseDTO;
import com.upc.compraamigo.security.entities.User;
import com.upc.compraamigo.security.services.CustomUserDetailsService;
import com.upc.compraamigo.security.services.UserService;
import com.upc.compraamigo.security.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          CustomUserDetailsService userDetailsService,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseDTO> createAuthenticationToken(@RequestBody AuthRequestDTO authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String token = jwtUtil.generateToken(userDetails);
        final User user = userService.buscarPorUsername(authRequest.getUsername());

        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", token);
        AuthResponseDTO response = new AuthResponseDTO();
        response.setJwt(token);
        response.setUserId(user.getId());
        response.setNombre(user.getNombre());
        response.setUsername(user.getUsername());
        response.setRoles(roles);

        return ResponseEntity.ok().headers(responseHeaders).body(response);
    }
}
