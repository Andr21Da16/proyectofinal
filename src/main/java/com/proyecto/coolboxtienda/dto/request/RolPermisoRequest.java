package com.proyecto.coolboxtienda.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolPermisoRequest {

    @NotNull(message = "El ID del rol es obligatorio")
    private Integer idRol;

    @NotBlank(message = "El nombre del módulo es obligatorio")
    private String nombreModulo;

    private Boolean puedeVer = false;
    private Boolean puedeEditar = false;
    private Boolean puedeCrear = false;
    private Boolean puedeEliminar = false;
}
