package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
    List<ChatRoom> findByActivoTrue();

    @Query("SELECT cr FROM ChatRoom cr JOIN ChatParticipant cp ON cr.idRoom = cp.room.idRoom " +
            "WHERE cp.colaborador.idColaborador = :idColaborador AND cr.activo = true")
    List<ChatRoom> findRoomsByColaborador(@Param("idColaborador") Integer idColaborador);
}
