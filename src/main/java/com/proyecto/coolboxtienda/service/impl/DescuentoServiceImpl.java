package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.DescuentoRequest;
import com.proyecto.coolboxtienda.dto.response.DescuentoResponse;
import com.proyecto.coolboxtienda.entity.Descuento;
import com.proyecto.coolboxtienda.entity.Producto;
import com.proyecto.coolboxtienda.mapper.EntityMapper;
import com.proyecto.coolboxtienda.repository.DescuentoRepository;
import com.proyecto.coolboxtienda.repository.ProductoRepository;
import com.proyecto.coolboxtienda.service.DescuentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DescuentoServiceImpl implements DescuentoService {

    private final DescuentoRepository descuentoRepository;
    private final ProductoRepository productoRepository;
    private final EntityMapper entityMapper;

    @Override
    @Transactional
    public DescuentoResponse createDescuento(DescuentoRequest request) {
        Producto producto = productoRepository.findById(request.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Descuento descuento = entityMapper.toDescuentoEntity(request, producto);
        descuento = descuentoRepository.save(descuento);

        return entityMapper.toDescuentoResponse(descuento);
    }

    @Override
    @Transactional
    public DescuentoResponse updateDescuento(Integer id, DescuentoRequest request) {
        Descuento descuento = descuentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Descuento no encontrado"));

        entityMapper.updateDescuentoEntity(descuento, request);
        descuento = descuentoRepository.save(descuento);

        return entityMapper.toDescuentoResponse(descuento);
    }

    @Override
    @Transactional
    public void deleteDescuento(Integer id) {
        Descuento descuento = descuentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Descuento no encontrado"));
        descuento.setActivo(false);
        descuentoRepository.save(descuento);
    }

    @Override
    @Transactional(readOnly = true)
    public DescuentoResponse getDescuentoById(Integer id) {
        Descuento descuento = descuentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Descuento no encontrado"));
        return entityMapper.toDescuentoResponse(descuento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DescuentoResponse> getDescuentosActivos() {
        LocalDateTime now = LocalDateTime.now();
        return descuentoRepository.findDescuentosActivos(now).stream()
                .map(entityMapper::toDescuentoResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DescuentoResponse getDescuentoByProducto(Integer idProducto) {
        LocalDateTime now = LocalDateTime.now();
        return descuentoRepository.findDescuentoActivoByProducto(idProducto, now)
                .map(entityMapper::toDescuentoResponse)
                .orElse(null);
    }
}
