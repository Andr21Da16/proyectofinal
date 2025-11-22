package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.Garantia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GarantiaRepository extends JpaRepository<Garantia, Integer> {

    List<Garantia> findByCliente_IdCliente(Integer idCliente);

    List<Garantia> findByEstado(String estado);

    List<Garantia> findByColaboradorAsignado_IdColaborador(Integer idColaborador);

    @Query("SELECT g FROM Garantia g WHERE " +
            "(:idCliente IS NULL OR g.cliente.idCliente = :idCliente) AND " +
            "(:estado IS NULL OR g.estado = :estado) AND " +
            "(:idProducto IS NULL OR g.producto.idProducto = :idProducto)")
    List<Garantia> findByFilters(@Param("idCliente") Integer idCliente,
            @Param("estado") String estado,
            @Param("idProducto") Integer idProducto);
}
