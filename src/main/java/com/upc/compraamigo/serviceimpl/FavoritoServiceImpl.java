package com.upc.compraamigo.serviceimpl;

import com.upc.compraamigo.dtos.FavoritoDTO;
import com.upc.compraamigo.entities.Favorito;
import com.upc.compraamigo.entities.Producto;
import com.upc.compraamigo.repositories.FavoritoRepositorio;
import com.upc.compraamigo.repositories.ProductoRepositorio;
import com.upc.compraamigo.security.entities.User;
import com.upc.compraamigo.security.repositories.UserRepository;
import com.upc.compraamigo.services.FavoritoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoritoServiceImpl implements FavoritoService {

    @Autowired
    private FavoritoRepositorio favoritoRepositorio;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductoRepositorio productoRepositorio;

    @Transactional
    @Override
    public FavoritoDTO agregarFavorito(FavoritoDTO favoritoDTO) {

        User user = userRepository.findById(favoritoDTO.getUserID())
                .orElseThrow(() -> new RuntimeException(
                        "Usuario no encontrado con ID: " + favoritoDTO.getUserID()
                ));

        Producto producto = productoRepositorio.findById(favoritoDTO.getProductoID())
                .orElseThrow(() -> new RuntimeException(
                        "Producto no encontrado con ID: " + favoritoDTO.getProductoID()
                ));

        if (favoritoRepositorio.existsByUserIdAndProductoId(
                favoritoDTO.getUserID(),
                favoritoDTO.getProductoID())) {
            throw new RuntimeException("El producto ya se encuentra en favoritos");
        }
        Favorito favorito = new Favorito();
        favorito.setUser(user);
        favorito.setProducto(producto);
        Favorito guardado = favoritoRepositorio.save(favorito);
        return convertirDTO(guardado);
    }

    @Override
    public List<FavoritoDTO> listarFavoritosPorUsuario(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException(
                    "Usuario no encontrado con ID: " + userId
            );
        }
        return favoritoRepositorio.findByUserId(userId)
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    @Transactional
    @Override
    public void eliminarFavorito(Long id) {
        if (!favoritoRepositorio.existsById(id)) {
            throw new RuntimeException(
                    "Favorito no encontrado con ID: " + id
            );
        }
        favoritoRepositorio.deleteById(id);
    }

    private FavoritoDTO convertirDTO(Favorito favorito) {
        FavoritoDTO dto = new FavoritoDTO();
        dto.setId(favorito.getId());
        dto.setUserID(favorito.getUser().getId());
        dto.setProductoID(favorito.getProducto().getId());
        dto.setNombreProducto(favorito.getProducto().getNombre());
        dto.setPrecio(favorito.getProducto().getPrecio());
        dto.setCo2(favorito.getProducto().getCo2());
        return dto;
    }
}