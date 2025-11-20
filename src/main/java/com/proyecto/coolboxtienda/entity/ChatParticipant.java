package com.proyecto.coolboxtienda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_participants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ChatParticipantId.class)
public class ChatParticipant {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_room", nullable = false)
    private ChatRoom room;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_colaborador", nullable = false)
    private Colaborador colaborador;

    @Column(name = "fecha_union")
    private LocalDateTime fechaUnion = LocalDateTime.now();

    @Column(name = "activo")
    private Boolean activo = true;
}
