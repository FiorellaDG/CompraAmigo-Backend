package com.upc.compraamigo.services;

import com.upc.compraamigo.dtos.FavoritoDTO;
import java.util.List;

public interface FavoritoService {
    FavoritoDTO agregarFavorito(FavoritoDTO favoritoDTO);
    List<FavoritoDTO> listarFavoritosPorUsuario(Long userId);
    void eliminarFavorito(Long id);
}