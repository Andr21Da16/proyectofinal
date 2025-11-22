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
public class GarantiaResponse {
    private Integer idGarantia;
    private Integer idVenta;
    private Integer idPedido;
    private Integer idProducto;
    private String nombreProducto;
    private Integer idCliente;
    private String nombreCliente;
    private String descripcionProblema;
    private LocalDateTime fechaCompra;
    private LocalDateTime fechaReporte;
    private String estado;
    private Integer idColaboradorAsignado;
    private String nombreColaboradorAsignado;
    private String observaciones;
    private LocalDateTime fechaResolucion;
}
