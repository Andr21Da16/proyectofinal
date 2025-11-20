package com.proyecto.coolboxtienda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColaboradorResponse {
    private Integer idColaborador;
    private String nombreColaborador;
    private String emailColaborador;
    private String numeroColaborador;
    private String usuarioColaborador;
    private String rolNombre;
    private Integer idRol;
    private String sucursalNombre;
    private Integer idSucursal;
    private Boolean activo;
}
