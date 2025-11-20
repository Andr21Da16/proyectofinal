package com.proyecto.coolboxtienda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_online_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatOnlineStatus {

    @Id
    @Column(name = "id_colaborador")
    private Integer idColaborador;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_colaborador")
    private Colaborador colaborador;

    @Column(name = "online")
    private Boolean online = false;

    @Column(name = "ultima_actividad")
    private LocalDateTime ultimaActividad = LocalDateTime.now();
}
