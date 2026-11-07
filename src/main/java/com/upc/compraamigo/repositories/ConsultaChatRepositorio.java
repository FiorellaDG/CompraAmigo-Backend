package com.upc.compraamigo.repositories;

import com.upc.compraamigo.entities.ConsultaChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaChatRepositorio extends JpaRepository<ConsultaChat, Long> {
}
