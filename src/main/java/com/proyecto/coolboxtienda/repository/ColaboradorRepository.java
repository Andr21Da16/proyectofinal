package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Integer> {
    Optional<Colaborador> findByUsuarioColaborador(String usuarioColaborador);

    Optional<Colaborador> findByEmailColaborador(String emailColaborador);

    List<Colaborador> findByActivoTrue();

    List<Colaborador> findBySucursal_IdSucursal(Integer idSucursal);

    List<Colaborador> findBySucursal_IdSucursalAndActivoTrue(Integer idSucursal);

    List<Colaborador> findByRol_IdRol(Integer idRol);

    List<Colaborador> findByRol_IdRolAndActivoTrue(Integer idRol);

    boolean existsByUsuarioColaborador(String usuarioColaborador);

    boolean existsByEmailColaborador(String emailColaborador);
}
