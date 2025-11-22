package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.CompraProveedorItem;
import com.proyecto.coolboxtienda.entity.CompraProveedorItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraProveedorItemRepository extends JpaRepository<CompraProveedorItem, CompraProveedorItemId> {

    List<CompraProveedorItem> findByCompraProveedor_IdCompra(Integer idCompra);
}
