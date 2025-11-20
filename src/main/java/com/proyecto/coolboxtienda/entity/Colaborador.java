package com.proyecto.coolboxtienda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "colaborador")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_colaborador")
    private Integer idColaborador;

    @Column(name = "nombre_colaborador", nullable = false, length = 100)
    private String nombreColaborador;

    @Column(name = "email_colaborador", nullable = false, unique = true, length = 300)
    private String emailColaborador;

    @Column(name = "numero_colaborador", length = 20)
    private String numeroColaborador;

    @Column(name = "usuario_colaborador", nullable = false, unique = true, length = 50)
    private String usuarioColaborador;

    @Column(name = "contraseña_colaborador", nullable = false, length = 255)
    private String contraseñaColaborador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sucursal")
    private Sucursal sucursal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;
}
