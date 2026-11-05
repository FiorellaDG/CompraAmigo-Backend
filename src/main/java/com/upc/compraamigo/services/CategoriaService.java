package com.upc.compraamigo.services;

import com.upc.compraamigo.dtos.CategoriaDTO;
import java.util.List;

public interface CategoriaService {
    CategoriaDTO insertarCategoria(CategoriaDTO categoriaDTO);
    List<CategoriaDTO> listarCategorias();
    CategoriaDTO buscarCategoriaPorId(long id);
}
