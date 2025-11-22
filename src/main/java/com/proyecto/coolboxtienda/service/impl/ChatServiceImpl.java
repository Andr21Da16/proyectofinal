package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.ChatMessageRequest;
import com.proyecto.coolboxtienda.dto.request.ChatRoomRequest;
import com.proyecto.coolboxtienda.dto.response.ChatMessageResponse;
import com.proyecto.coolboxtienda.dto.response.ChatRoomResponse;
import com.proyecto.coolboxtienda.entity.*;
import com.proyecto.coolboxtienda.mapper.EntityMapper;
import com.proyecto.coolboxtienda.repository.*;
import com.proyecto.coolboxtienda.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

        private final ChatRoomRepository chatRoomRepository;
        private final ChatMessageRepository chatMessageRepository;
        private final ChatParticipantRepository chatParticipantRepository;
        private final ColaboradorRepository colaboradorRepository;
        private final EntityMapper entityMapper;

        @Override
        @Transactional
        public ChatRoomResponse createRoom(ChatRoomRequest request) {
                // Crear sala
                ChatRoom room = entityMapper.toChatRoomEntity(request);
                room = chatRoomRepository.save(room);

                // Agregar participantes
                for (Integer idColaborador : request.getParticipantes()) {
                        Colaborador colaborador = colaboradorRepository.findById(idColaborador)
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Colaborador no encontrado: " + idColaborador));

                        ChatParticipant participant = new ChatParticipant();
                        // Set composite key fields directly (no setId() for @IdClass)
                        participant.setRoom(room);
                        participant.setColaborador(colaborador);
                        participant.setActivo(true);
                        chatParticipantRepository.save(participant);
                }

                List<ChatParticipant> participants = chatParticipantRepository.findByRoom_IdRoom(room.getIdRoom());
                return entityMapper.toChatRoomResponse(room, participants, null, 0L);
        }

        @Override
        @Transactional(readOnly = true)
        public List<ChatRoomResponse> getRoomsByColaborador(Integer idColaborador) {
                List<ChatParticipant> participations = chatParticipantRepository
                                .findByColaborador_IdColaboradorAndActivoTrue(idColaborador);

                return participations.stream()
                                .map(participation -> {
                                        ChatRoom room = participation.getRoom();
                                        List<ChatParticipant> participants = chatParticipantRepository
                                                        .findByRoom_IdRoom(room.getIdRoom());
                                        ChatMessage ultimoMensaje = chatMessageRepository
                                                        .findTopByRoom_IdRoomOrderByFechaEnvioDesc(room.getIdRoom())
                                                        .orElse(null);
                                        Long mensajesNoLeidos = chatMessageRepository
                                                        .countUnreadMessages(room.getIdRoom(), idColaborador);
                                        return entityMapper.toChatRoomResponse(room, participants, ultimoMensaje,
                                                        mensajesNoLeidos);
                                })
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public ChatMessageResponse sendMessage(ChatMessageRequest request) {
                ChatRoom room = chatRoomRepository.findById(request.getIdRoom())
                                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

                Colaborador colaborador = colaboradorRepository.findById(request.getIdColaborador())
                                .orElseThrow(() -> new RuntimeException("Colaborador no encontrado"));

                ChatMessage message = entityMapper.toChatMessageEntity(request, room, colaborador);
                message = chatMessageRepository.save(message);

                return entityMapper.toChatMessageResponse(message);
        }

        @Override
        @Transactional(readOnly = true)
        public List<ChatMessageResponse> getMessagesByRoom(Integer idRoom, Integer limit) {
                List<ChatMessage> messages = chatMessageRepository.findByRoom_IdRoomOrderByFechaEnvioDesc(
                                idRoom, PageRequest.of(0, limit != null ? limit : 50));

                return messages.stream()
                                .map(entityMapper::toChatMessageResponse)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public void markAsRead(Integer idMensaje) {
                ChatMessage message = chatMessageRepository.findById(idMensaje)
                                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));
                message.setLeido(true);
                chatMessageRepository.save(message);
        }

        @Override
        @Transactional
        public void addParticipant(Integer idRoom, Integer idColaborador) {
                ChatRoom room = chatRoomRepository.findById(idRoom)
                                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

                Colaborador colaborador = colaboradorRepository.findById(idColaborador)
                                .orElseThrow(() -> new RuntimeException("Colaborador no encontrado"));

                ChatParticipant participant = new ChatParticipant();
                // Set composite key fields directly (no setId() for @IdClass)
                participant.setRoom(room);
                participant.setColaborador(colaborador);
                participant.setActivo(true);
                chatParticipantRepository.save(participant);
        }

        @Override
        @Transactional
        public void removeParticipant(Integer idRoom, Integer idColaborador) {
                ChatParticipantId participantId = new ChatParticipantId(idRoom, idColaborador);
                ChatParticipant participant = chatParticipantRepository.findById(participantId)
                                .orElseThrow(() -> new RuntimeException("Participante no encontrado"));
                participant.setActivo(false);
                chatParticipantRepository.save(participant);
        }

        @Override
        @Transactional(readOnly = true)
        public Integer getUnreadCount(Integer idRoom, Integer idColaborador) {
                Long count = chatMessageRepository.countUnreadMessages(idRoom, idColaborador);
                return count != null ? count.intValue() : 0;
        }

        @Override
        @Transactional
        public void markAsRead(Integer idRoom, Integer idColaborador) {
                // Mark all messages in room as read for this collaborator
                List<ChatMessage> messages = chatMessageRepository.findByRoom_IdRoomOrderByFechaEnvioDesc(
                                idRoom, PageRequest.of(0, 1000));
                for (ChatMessage message : messages) {
                        if (!message.getColaborador().getIdColaborador().equals(idColaborador) && !message.getLeido()) {
                                message.setLeido(true);
                                chatMessageRepository.save(message);
                        }
                }
        }

        @Override
        @Transactional
        public void pinMessage(Integer idMensaje) {
                ChatMessage message = chatMessageRepository.findById(idMensaje)
                                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));
                message.setEsFijado(true);
                chatMessageRepository.save(message);
        }

        @Override
        @Transactional
        public void unpinMessage(Integer idMensaje) {
                ChatMessage message = chatMessageRepository.findById(idMensaje)
                                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));
                message.setEsFijado(false);
                chatMessageRepository.save(message);
        }
}
