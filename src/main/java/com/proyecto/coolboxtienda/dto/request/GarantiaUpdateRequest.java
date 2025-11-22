package com.proyecto.coolboxtienda.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GarantiaUpdateRequest {

    @NotBlank(message = "El estado es obligatorio")
    private String estado; // REGISTRADA, EN_REVISION, EN_REPARACION, LISTA_EN_TIENDA, ENTREGADA, FALLIDA

    private String observaciones;
}
