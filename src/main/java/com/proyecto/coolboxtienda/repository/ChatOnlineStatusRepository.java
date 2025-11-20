package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.ChatOnlineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatOnlineStatusRepository extends JpaRepository<ChatOnlineStatus, Integer> {
    List<ChatOnlineStatus> findByOnlineTrue();
}
