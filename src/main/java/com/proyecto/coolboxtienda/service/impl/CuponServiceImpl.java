package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.CuponRequest;
import com.proyecto.coolboxtienda.dto.response.CuponResponse;
import com.proyecto.coolboxtienda.entity.Cupon;
import com.proyecto.coolboxtienda.repository.CuponRepository;
import com.proyecto.coolboxtienda.service.CuponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CuponServiceImpl implements CuponService {

    private final CuponRepository cuponRepository;

    @Override
    public List<CuponResponse> getAllCupones() {
        return cuponRepository.findAll().stream()
                .map(this::toCuponResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CuponResponse> getCuponesActivos() {
        return cuponRepository.findByActivoTrue().stream()
                .map(this::toCuponResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CuponResponse getCuponById(Integer id) {
        Cupon cupon = cuponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cupón no encontrado"));
        return toCuponResponse(cupon);
    }

    @Override
    public CuponResponse getCuponByCodigo(String codigo) {
        Cupon cupon = cuponRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Cupón no encontrado"));
        return toCuponResponse(cupon);
    }

    @Override
    @Transactional
    public CuponResponse createCupon(CuponRequest request) {
        // Validar que el código no exista
        if (cuponRepository.findByCodigo(request.getCodigo()).isPresent()) {
            throw new RuntimeException("El código del cupón ya existe");
        }

        Cupon cupon = new Cupon();
        cupon.setCodigo(request.getCodigo());
        cupon.setDescripcion(request.getDescripcion());
        cupon.setDescuentoPorcentaje(request.getDescuentoPorcentaje());
        cupon.setDescuentoMonto(request.getDescuentoMonto());
        cupon.setFechaInicio(request.getFechaInicio());
        cupon.setFechaFin(request.getFechaFin());
        cupon.setUsoMaximo(request.getUsoMaximo());
        cupon.setUsoActual(0);
        cupon.setActivo(request.getActivo());
        cupon.setMontoMinimo(request.getMontoMinimo());

        cupon = cuponRepository.save(cupon);
        return toCuponResponse(cupon);
    }

    @Override
    @Transactional
    public CuponResponse updateCupon(Integer id, CuponRequest request) {
        Cupon cupon = cuponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cupón no encontrado"));

        cupon.setDescripcion(request.getDescripcion());
        cupon.setDescuentoPorcentaje(request.getDescuentoPorcentaje());
        cupon.setDescuentoMonto(request.getDescuentoMonto());
        cupon.setFechaInicio(request.getFechaInicio());
        cupon.setFechaFin(request.getFechaFin());
        cupon.setUsoMaximo(request.getUsoMaximo());
        cupon.setActivo(request.getActivo());
        cupon.setMontoMinimo(request.getMontoMinimo());

        cupon = cuponRepository.save(cupon);
        return toCuponResponse(cupon);
    }

    @Override
    @Transactional
    public void deleteCupon(Integer id) {
        Cupon cupon = cuponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cupón no encontrado"));
        cupon.setActivo(false);
        cuponRepository.save(cupon);
    }

    @Override
    public boolean validarCupon(String codigo, BigDecimal montoCompra) {
        return cuponRepository.findValidCupon(codigo, java.time.LocalDateTime.now()).isPresent();
    }

    private CuponResponse toCuponResponse(Cupon cupon) {
        return CuponResponse.builder()
                .idCupon(cupon.getIdCupon())
                .codigo(cupon.getCodigo())
                .descripcion(cupon.getDescripcion())
                .descuentoPorcentaje(cupon.getDescuentoPorcentaje())
                .descuentoMonto(cupon.getDescuentoMonto())
                .fechaInicio(cupon.getFechaInicio())
                .fechaFin(cupon.getFechaFin())
                .usoMaximo(cupon.getUsoMaximo())
                .usoActual(cupon.getUsoActual())
                .activo(cupon.getActivo())
                .montoMinimo(cupon.getMontoMinimo())
                .build();
    }
}
