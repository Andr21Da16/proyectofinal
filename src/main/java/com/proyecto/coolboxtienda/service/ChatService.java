package com.proyecto.coolboxtienda.service;

import com.proyecto.coolboxtienda.dto.request.ChatMessageRequest;
import com.proyecto.coolboxtienda.dto.request.ChatRoomRequest;
import com.proyecto.coolboxtienda.dto.response.ChatMessageResponse;
import com.proyecto.coolboxtienda.dto.response.ChatRoomResponse;

import java.util.List;

public interface ChatService {
    ChatRoomResponse createRoom(ChatRoomRequest request);

    List<ChatRoomResponse> getRoomsByColaborador(Integer idColaborador);

    ChatMessageResponse sendMessage(ChatMessageRequest request);

    List<ChatMessageResponse> getMessagesByRoom(Integer idRoom, Integer limit);

    void markAsRead(Integer idMensaje);

    void addParticipant(Integer idRoom, Integer idColaborador);

    void removeParticipant(Integer idRoom, Integer idColaborador);

    Integer getUnreadCount(Integer idRoom, Integer idColaborador);

    void markAsRead(Integer idRoom, Integer idColaborador);

    void pinMessage(Integer idMensaje);

    void unpinMessage(Integer idMensaje);
}
