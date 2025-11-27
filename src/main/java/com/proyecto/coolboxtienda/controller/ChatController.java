package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.request.ChatRoomRequest;
import com.proyecto.coolboxtienda.dto.response.ChatMessageResponse;
import com.proyecto.coolboxtienda.dto.response.ChatRoomResponse;
import com.proyecto.coolboxtienda.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor

public class ChatController {

    private final ChatService chatService;

    @PostMapping("/rooms")
    public ResponseEntity<ChatRoomResponse> createRoom(@Valid @RequestBody ChatRoomRequest request) {
        return ResponseEntity.ok(chatService.createRoom(request));
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponse>> getRoomsByColaborador(@RequestParam Integer idColaborador) {
        return ResponseEntity.ok(chatService.getRoomsByColaborador(idColaborador));
    }

    @GetMapping("/rooms/{id}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getMessagesByRoom(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "50") Integer limit) {
        return ResponseEntity.ok(chatService.getMessagesByRoom(id, limit));
    }

    @PostMapping("/rooms/{id}/participants")
    public ResponseEntity<Void> addParticipant(
            @PathVariable Integer id,
            @RequestParam Integer idColaborador) {
        chatService.addParticipant(id, idColaborador);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rooms/{id}/unread-count")
    public ResponseEntity<Integer> getUnreadCount(@PathVariable Integer id, @RequestParam Integer idColaborador) {
        return ResponseEntity.ok(chatService.getUnreadCount(id, idColaborador));
    }

    @PutMapping("/rooms/{id}/mark-read")
    public ResponseEntity<Void> markAsRead(@PathVariable Integer id, @RequestParam Integer idColaborador) {
        chatService.markAsRead(id, idColaborador);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/messages/{id}/pin")
    public ResponseEntity<Void> pinMessage(@PathVariable Integer id) {
        chatService.pinMessage(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/messages/{id}/unpin")
    public ResponseEntity<Void> unpinMessage(@PathVariable Integer id) {
        chatService.unpinMessage(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/messages")
    public ResponseEntity<ChatMessageResponse> sendMessage(
            @Valid @RequestBody com.proyecto.coolboxtienda.dto.request.ChatMessageRequest request) {
        return ResponseEntity.ok(chatService.sendMessage(request));
    }
}
