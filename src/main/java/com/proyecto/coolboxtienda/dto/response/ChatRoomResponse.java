package com.proyecto.coolboxtienda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponse {
    private Integer idRoom;
    private String nombreRoom;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private Boolean activo;
    private List<ParticipanteResponse> participantes;
    private Long mensajesNoLeidos;
    private ChatMessageResponse ultimoMensaje;
}
