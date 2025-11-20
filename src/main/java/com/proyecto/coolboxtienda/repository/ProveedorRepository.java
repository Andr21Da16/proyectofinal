package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
    Optional<Proveedor> findByNombreProveedor(String nombreProveedor);

    Optional<Proveedor> findByEmailProveedor(String emailProveedor);

    boolean existsByEmailProveedor(String emailProveedor);

    boolean existsByTelefonoProveedor(String telefonoProveedor);
}
