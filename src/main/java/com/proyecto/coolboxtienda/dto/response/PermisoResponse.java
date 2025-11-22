package com.proyecto.coolboxtienda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermisoResponse {
    private String nombreModulo;
    private Boolean puedeVer;
    private Boolean puedeEditar;
    private Boolean puedeCrear;
    private Boolean puedeEliminar;
}
