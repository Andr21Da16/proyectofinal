package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.CompraProveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraProveedorRepository extends JpaRepository<CompraProveedor, Integer> {

    List<CompraProveedor> findByProveedor_IdProveedor(Integer idProveedor);

    List<CompraProveedor> findByEstado(String estado);

    @Query("SELECT cp FROM CompraProveedor cp WHERE " +
            "(:idProveedor IS NULL OR cp.proveedor.idProveedor = :idProveedor) AND " +
            "(:estado IS NULL OR cp.estado = :estado) AND " +
            "(:idSucursal IS NULL OR cp.sucursal.idSucursal = :idSucursal)")
    List<CompraProveedor> findByFilters(@Param("idProveedor") Integer idProveedor,
            @Param("estado") String estado,
            @Param("idSucursal") Integer idSucursal);
}
