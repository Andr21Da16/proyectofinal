package com.proyecto.coolboxtienda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {
    private Integer idMensaje;
    private Integer idRoom;
    private String nombreColaborador;
    private Integer idColaborador;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private Boolean leido;
}
