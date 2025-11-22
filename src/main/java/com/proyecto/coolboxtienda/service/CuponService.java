package com.proyecto.coolboxtienda.service;

import com.proyecto.coolboxtienda.dto.request.CuponRequest;
import com.proyecto.coolboxtienda.dto.response.CuponResponse;

import java.math.BigDecimal;
import java.util.List;

public interface CuponService {
    List<CuponResponse> getAllCupones();

    List<CuponResponse> getCuponesActivos();

    CuponResponse getCuponById(Integer id);

    CuponResponse getCuponByCodigo(String codigo);

    CuponResponse createCupon(CuponRequest request);

    CuponResponse updateCupon(Integer id, CuponRequest request);

    void deleteCupon(Integer id);

    boolean validarCupon(String codigo, BigDecimal montoCompra);
}
