package com.upc.compraamigo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@Profile("render")
public class SpaRenderConfig implements WebMvcConfigurer {
    public static final List<String> RUTAS = List.of(
            "/", "/home", "/login", "/catalogo", "/producto/{id}", "/perfil",
            "/carrito", "/checkout", "/pedidos", "/pedidos/{id}",
            "/pedidos/{id}/confirmacion", "/soporte", "/admin/productos", "/admin/pedidos"
    );

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/favicon.ico", "/logo_compraamigo.png");
        for (String ruta : RUTAS) {
            registry.addViewController(ruta).setViewName("forward:/index.html");
        }
    }
}
