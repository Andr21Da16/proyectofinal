package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByCliente_IdCliente(Integer idCliente);

    List<Pedido> findByEstadoLogistico(String estadoLogistico);

    List<Pedido> findByEstadoPago(String estadoPago);

    @Query("SELECT p FROM Pedido p WHERE " +
            "(:idCliente IS NULL OR p.cliente.idCliente = :idCliente) AND " +
            "(:estadoLogistico IS NULL OR p.estadoLogistico = :estadoLogistico) AND " +
            "(:estadoPago IS NULL OR p.estadoPago = :estadoPago) AND " +
            "(:idSucursal IS NULL OR p.sucursal.idSucursal = :idSucursal)")
    List<Pedido> findByFilters(@Param("idCliente") Integer idCliente,
            @Param("estadoLogistico") String estadoLogistico,
            @Param("estadoPago") String estadoPago,
            @Param("idSucursal") Integer idSucursal);

    @Query("SELECT p FROM Pedido p WHERE p.estadoLogistico IN ('PENDIENTE', 'EN_PREPARACION')")
    List<Pedido> findPedidosParaAlmacen();

    @Query("SELECT p FROM Pedido p WHERE p.estadoLogistico = 'EN_TRANSITO'")
    List<Pedido> findPedidosEnTransito();
}
