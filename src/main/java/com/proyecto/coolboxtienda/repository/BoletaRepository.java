package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Integer> {
    Optional<Boleta> findByNumeroBoleta(String numeroBoleta);

    Optional<Boleta> findByVenta_IdVenta(Integer idVenta);
}
