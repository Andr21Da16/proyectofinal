package com.proyecto.coolboxtienda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolPermisoId implements Serializable {

    @Column(name = "id_rol")
    private Integer idRol;

    @Column(name = "nombre_modulo", length = 100)
    private String nombreModulo;
}
