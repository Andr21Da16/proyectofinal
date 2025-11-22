package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.PedidoItem;
import com.proyecto.coolboxtienda.entity.PedidoItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoItemRepository extends JpaRepository<PedidoItem, PedidoItemId> {

    List<PedidoItem> findByPedido_IdPedido(Integer idPedido);
}
