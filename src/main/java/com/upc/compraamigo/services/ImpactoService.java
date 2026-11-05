package com.upc.compraamigo.services;

import com.upc.compraamigo.dtos.ImpactoCategoriaDTO;
import com.upc.compraamigo.dtos.ImpactoUsuarioDTO;

import java.util.List;

public interface ImpactoService {
    ImpactoUsuarioDTO obtenerImpactoUsuario(Long userId);
    List<ImpactoCategoriaDTO> obtenerImpactoPorCategoria();
}
