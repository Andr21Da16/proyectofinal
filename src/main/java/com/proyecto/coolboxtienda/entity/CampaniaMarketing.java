package com.proyecto.coolboxtienda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "campanias_marketing")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaniaMarketing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_campania")
    private Integer idCampania;

    @Column(name = "nombre_campania", nullable = false, length = 200)
    private String nombreCampania;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(name = "descuento_porcentaje", precision = 5, scale = 2)
    private BigDecimal descuentoPorcentaje;

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "tipo_descuento", length = 20)
    private String tipoDescuento = "PORCENTAJE"; // PORCENTAJE, MONTO_FIJO
}
