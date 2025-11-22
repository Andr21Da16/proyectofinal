package com.proyecto.coolboxtienda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampaniaResponse {
    private Integer idCampania;
    private String nombreCampania;
    private String descripcion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private BigDecimal descuentoPorcentaje;
    private Boolean activo;
    private String tipoDescuento;
    private List<ProductoCampaniaResponse> productos;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductoCampaniaResponse {
        private Integer idProducto;
        private String nombreProducto;
        private BigDecimal precioOriginal;
        private BigDecimal precioConDescuento;
    }
}
