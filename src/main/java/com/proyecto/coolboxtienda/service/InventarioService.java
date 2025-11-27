package com.proyecto.coolboxtienda.service;

import com.proyecto.coolboxtienda.dto.request.InventarioRequest;
import com.proyecto.coolboxtienda.dto.request.TransferenciaRequest;
import com.proyecto.coolboxtienda.dto.response.SucursalProductoResponse;

import java.util.List;

public interface InventarioService {
    void addStock(InventarioRequest request);

    void updateStock(InventarioRequest request);

    void transferStock(TransferenciaRequest request);

    List<SucursalProductoResponse> getInventoryBySucursal(Integer idSucursal);

    List<SucursalProductoResponse> getInventoryByProducto(Integer idProducto);

    List<SucursalProductoResponse> getLowStockProducts(Integer threshold);

    boolean checkAvailability(Integer idProducto, Integer idSucursal, Integer idProveedor, Integer cantidad);

    List<com.proyecto.coolboxtienda.dto.response.MovimientoInventarioResponse> getRecentMovements(Integer limit);
}
