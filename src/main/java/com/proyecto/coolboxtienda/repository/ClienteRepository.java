package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Optional<Cliente> findByEmail(String email);

    Optional<Cliente> findByNumeroDocumento(String numeroDocumento);

    @Query("SELECT c FROM Cliente c WHERE " +
            "(:nombre IS NULL OR LOWER(c.nombreCompleto) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
            "(:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:numeroDocumento IS NULL OR c.numeroDocumento = :numeroDocumento) AND " +
            "(:activo IS NULL OR c.activo = :activo)")
    List<Cliente> findByFilters(@Param("nombre") String nombre,
            @Param("email") String email,
            @Param("numeroDocumento") String numeroDocumento,
            @Param("activo") Boolean activo);

    List<Cliente> findByActivoTrue();
}
