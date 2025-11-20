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
public class ComprobanteResponse {
    private Integer idBoleta;
    private String numeroBoleta;
    private LocalDateTime fechaEmision;
    private String tipoComprobante;
    private VentaResponse venta;
}
