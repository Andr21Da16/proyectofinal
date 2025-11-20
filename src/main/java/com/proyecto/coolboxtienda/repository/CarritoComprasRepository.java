package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.CarritoCompras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoComprasRepository extends JpaRepository<CarritoCompras, Integer> {
    Optional<CarritoCompras> findByColaborador_IdColaboradorAndActivoTrue(Integer idColaborador);

    Optional<CarritoCompras> findByColaborador_IdColaborador(Integer idColaborador);
}
