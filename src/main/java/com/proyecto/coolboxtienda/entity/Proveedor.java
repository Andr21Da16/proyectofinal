package com.proyecto.coolboxtienda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "proveedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Integer idProveedor;

    @Column(name = "nombre_proveedor", nullable = false, unique = true, length = 100)
    private String nombreProveedor;

    @Column(name = "email_proveedor", nullable = false, unique = true, length = 300)
    private String emailProveedor;

    @Column(name = "telefono_proveedor", nullable = false, unique = true, length = 20)
    private String telefonoProveedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ciudad", nullable = false)
    private Ciudad ciudad;
}
