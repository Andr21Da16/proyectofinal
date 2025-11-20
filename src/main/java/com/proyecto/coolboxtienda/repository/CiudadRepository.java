package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, Integer> {
    Optional<Ciudad> findByNombreCiudad(String nombreCiudad);

    List<Ciudad> findByDepartamento_IdDepartamento(Integer idDepartamento);

    boolean existsByNombreCiudad(String nombreCiudad);
}
