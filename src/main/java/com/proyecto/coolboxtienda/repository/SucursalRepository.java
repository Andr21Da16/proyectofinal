package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {
    Optional<Sucursal> findByNombreSucursal(String nombreSucursal);

    List<Sucursal> findByActivoTrue();

    List<Sucursal> findByCiudad_IdCiudad(Integer idCiudad);

    List<Sucursal> findByCiudad_Departamento_IdDepartamentoAndActivoTrue(Integer idDepartamento);
}
