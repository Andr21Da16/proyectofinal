package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Integer> {
    Optional<Departamento> findByNombreDepartamento(String nombreDepartamento);

    boolean existsByNombreDepartamento(String nombreDepartamento);
}
