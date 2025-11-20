package com.proyecto.coolboxtienda.service;

import com.proyecto.coolboxtienda.dto.request.CarritoItemRequest;
import com.proyecto.coolboxtienda.dto.response.CarritoFullResponse;
import com.proyecto.coolboxtienda.dto.response.CarritoResponse;

public interface CarritoService {
    CarritoResponse getOrCreateCarrito(Integer idColaborador);

    CarritoFullResponse addItem(Integer idColaborador, CarritoItemRequest request);

    CarritoFullResponse updateItemQuantity(Integer idColaborador, Integer idProducto, Integer idProveedor,
            Integer idSucursal, Integer cantidad);

    CarritoFullResponse removeItem(Integer idColaborador, Integer idProducto, Integer idProveedor, Integer idSucursal);

    void clearCarrito(Integer idColaborador);

    CarritoFullResponse getCarritoFull(Integer idColaborador);
}
