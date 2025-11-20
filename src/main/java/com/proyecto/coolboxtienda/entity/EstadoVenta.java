package com.proyecto.coolboxtienda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estado_venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_venta")
    private Integer idEstadoVenta;

    @Column(name = "nombre_estado", nullable = false, unique = true, length = 50)
    private String nombreEstado;

    @Column(name = "descripcion_estado", columnDefinition = "TEXT")
    private String descripcionEstado;
}
