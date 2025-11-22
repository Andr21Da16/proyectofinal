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
public class ClienteResponse {
    private Integer idCliente;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private String direccion;
    private String tipoDocumento;
    private String numeroDocumento;
    private String ciudadNombre;
    private Integer idCiudad;
    private LocalDateTime fechaRegistro;
    private Boolean activo;
}
