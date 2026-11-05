package com.upc.compraamigo.serviceimpl;

import com.upc.compraamigo.dtos.ConsultaChatDTO;
import com.upc.compraamigo.entities.ConsultaChat;
import com.upc.compraamigo.repositories.ConsultaChatRepositorio;
import com.upc.compraamigo.security.entities.User;
import com.upc.compraamigo.security.repositories.UserRepository;
import com.upc.compraamigo.services.ConsultaChatService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsultaChatServiceImpl implements ConsultaChatService {
    @Autowired
    private ConsultaChatRepositorio consultaChatRepositorio;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ConsultaChatDTO registrarConsulta(ConsultaChatDTO consultaChatDTO) {
        User user = userRepository.findById(consultaChatDTO.getUserID())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + consultaChatDTO.getUserID()));
        ConsultaChat consultaChat = modelMapper.map(consultaChatDTO, ConsultaChat.class);
        consultaChat.setId(null);
        consultaChat.setFecha(LocalDateTime.now());
        consultaChat.setUser(user);
        ConsultaChat guardada = consultaChatRepositorio.save(consultaChat);
        ConsultaChatDTO respuesta = modelMapper.map(guardada, ConsultaChatDTO.class);
        respuesta.setUserID(user.getId());
        return respuesta;
    }

    @Override
    public List<ConsultaChatDTO> listarConsultas() {
        return consultaChatRepositorio.findAll().stream().map(consulta -> {
            ConsultaChatDTO dto = modelMapper.map(consulta, ConsultaChatDTO.class);
            if (consulta.getUser() != null) dto.setUserID(consulta.getUser().getId());
            return dto;
        }).toList();
    }
}
