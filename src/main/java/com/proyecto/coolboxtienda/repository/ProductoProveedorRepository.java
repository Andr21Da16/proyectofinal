package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.ProductoProveedor;
import com.proyecto.coolboxtienda.entity.ProductoProveedorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoProveedorRepository extends JpaRepository<ProductoProveedor, ProductoProveedorId> {
    List<ProductoProveedor> findByProducto_IdProducto(Integer idProducto);

    List<ProductoProveedor> findByProveedor_IdProveedor(Integer idProveedor);

    Optional<ProductoProveedor> findByProducto_IdProductoAndProveedor_IdProveedor(Integer idProducto,
            Integer idProveedor);

    @Query("SELECT pp FROM ProductoProveedor pp WHERE pp.stockProducto < :minStock")
    List<ProductoProveedor> findByStockBajo(@Param("minStock") Integer minStock);
}
