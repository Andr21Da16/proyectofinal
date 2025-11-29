package com.proyecto.coolboxtienda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIResponse {
    private String respuesta;
    private boolean exito;
    private String actionExecuted; // Acción realizada (ej: CREATE_COLABORADOR)
    private Object structuredData; // Datos estructurados para tablas/gráficos
    private boolean requiresConfirmation; // Si necesita confirmación del usuario
    private String conversationId; // ID de la conversación actual
}
