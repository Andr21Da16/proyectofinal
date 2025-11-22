package com.proyecto.coolboxtienda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rol_permiso", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "id_rol", "nombre_modulo" })
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolPermiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_permiso")
    private Integer idPermiso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @Column(name = "nombre_modulo", nullable = false, length = 100)
    private String nombreModulo;

    @Column(name = "puede_ver")
    private Boolean puedeVer = false;

    @Column(name = "puede_editar")
    private Boolean puedeEditar = false;

    @Column(name = "puede_crear")
    private Boolean puedeCrear = false;

    @Column(name = "puede_eliminar")
    private Boolean puedeEliminar = false;
}
