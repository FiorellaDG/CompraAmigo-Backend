package com.upc.compraamigo.serviceimpl;

import com.upc.compraamigo.dtos.CategoriaDTO;
import com.upc.compraamigo.entities.Categoria;
import com.upc.compraamigo.repositories.CategoriaRepositorio;
import com.upc.compraamigo.services.CategoriaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {
    @Autowired
    private CategoriaRepositorio categoriaRepositorio;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoriaDTO insertarCategoria(CategoriaDTO categoriaDTO) {
        Categoria categoria = modelMapper.map(categoriaDTO, Categoria.class);
        return modelMapper.map(categoriaRepositorio.save(categoria), CategoriaDTO.class);
    }

    @Override
    public List<CategoriaDTO> listarCategorias() {
        return categoriaRepositorio.findAll().stream()
                .map(categoria -> modelMapper.map(categoria, CategoriaDTO.class))
                .toList();
    }

    @Override
    public CategoriaDTO buscarCategoriaPorId(long id) {
        return categoriaRepositorio.findById(id)
                .map(categoria -> modelMapper.map(categoria, CategoriaDTO.class))
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con ID: " + id));
    }
}
