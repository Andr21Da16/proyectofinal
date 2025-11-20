package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.ChatParticipant;
import com.proyecto.coolboxtienda.entity.ChatParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, ChatParticipantId> {
    List<ChatParticipant> findByRoom_IdRoom(Integer idRoom);

    List<ChatParticipant> findByColaborador_IdColaborador(Integer idColaborador);

    List<ChatParticipant> findByRoom_IdRoomAndActivoTrue(Integer idRoom);

    List<ChatParticipant> findByColaborador_IdColaboradorAndActivoTrue(Integer idColaborador);
}
