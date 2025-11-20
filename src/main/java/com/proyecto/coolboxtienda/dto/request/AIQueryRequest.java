package com.proyecto.coolboxtienda.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIQueryRequest {

    @NotBlank(message = "La consulta no puede estar vacía")
    @Size(max = 2000, message = "La consulta no puede exceder 2000 caracteres")
    private String query;

    private String context; // Contexto adicional (ej: datos de ventas, productos, etc.)
}
