package com.proyecto.coolboxtienda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rol_permiso")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolPermiso {

    @EmbeddedId
    private RolPermisoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idRol")
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @Column(name = "puede_ver")
    private Boolean puedeVer = false;

    @Column(name = "puede_editar")
    private Boolean puedeEditar = false;

    @Column(name = "puede_crear")
    private Boolean puedeCrear = false;

    @Column(name = "puede_eliminar")
    private Boolean puedeEliminar = false;
}
