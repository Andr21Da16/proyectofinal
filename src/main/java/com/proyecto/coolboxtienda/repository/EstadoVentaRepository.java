package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.EstadoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoVentaRepository extends JpaRepository<EstadoVenta, Integer> {
    Optional<EstadoVenta> findByNombreEstado(String nombreEstado);
}
