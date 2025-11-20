package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    List<ChatMessage> findByRoom_IdRoomOrderByFechaEnvioAsc(Integer idRoom);

    List<ChatMessage> findByRoom_IdRoomOrderByFechaEnvioDesc(Integer idRoom,
            org.springframework.data.domain.Pageable pageable);

    java.util.Optional<ChatMessage> findTopByRoom_IdRoomOrderByFechaEnvioDesc(Integer idRoom);

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.room.idRoom = :idRoom AND cm.leido = false")
    List<ChatMessage> findUnreadMessagesByRoom(@Param("idRoom") Integer idRoom);

    @Query("SELECT COUNT(cm) FROM ChatMessage cm WHERE cm.room.idRoom = :idRoom AND " +
            "cm.colaborador.idColaborador != :idColaborador AND cm.leido = false")
    Long countUnreadMessages(@Param("idRoom") Integer idRoom, @Param("idColaborador") Integer idColaborador);
}
