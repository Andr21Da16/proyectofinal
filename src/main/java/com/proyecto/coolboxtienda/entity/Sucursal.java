package com.proyecto.coolboxtienda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "sucursales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sucursal")
    private Integer idSucursal;

    @Column(name = "nombre_sucursal", nullable = false, unique = true, length = 100)
    private String nombreSucursal;

    @Column(name = "direccion_sucursal", nullable = false, columnDefinition = "TEXT")
    private String direccionSucursal;

    @Column(name = "telefono_sucursal", length = 20)
    private String telefonoSucursal;

    @Column(name = "email_sucursal", length = 150)
    private String emailSucursal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ciudad", nullable = false)
    private Ciudad ciudad;

    @Column(name = "fecha_apertura")
    private LocalDate fechaApertura;

    @Column(name = "activo")
    private Boolean activo = true;
}
