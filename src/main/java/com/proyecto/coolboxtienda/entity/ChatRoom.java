package com.proyecto.coolboxtienda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_room")
    private Integer idRoom;

    @Column(name = "nombre_room", nullable = false, length = 100)
    private String nombreRoom;

    @Column(name = "tipo_room", nullable = false, length = 20)
    private String tipoRoom; // INDIVIDUAL, GRUPO

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "activo")
    private Boolean activo = true;
}
