package com.upc.compraamigo.services;

import com.upc.compraamigo.dtos.ConsultaChatDTO;
import java.util.List;

public interface ConsultaChatService {
    ConsultaChatDTO registrarConsulta(ConsultaChatDTO consultaChatDTO);
    List<ConsultaChatDTO> listarConsultas();
}
