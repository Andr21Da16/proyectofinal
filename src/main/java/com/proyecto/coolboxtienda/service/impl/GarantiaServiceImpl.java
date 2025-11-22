package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.GarantiaUpdateRequest;
import com.proyecto.coolboxtienda.dto.response.GarantiaResponse;
import com.proyecto.coolboxtienda.entity.Garantia;
import com.proyecto.coolboxtienda.repository.ColaboradorRepository;
import com.proyecto.coolboxtienda.repository.GarantiaRepository;
import com.proyecto.coolboxtienda.service.GarantiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GarantiaServiceImpl implements GarantiaService {

    private final GarantiaRepository garantiaRepository;
    private final ColaboradorRepository colaboradorRepository;

    @Override
    public List<GarantiaResponse> getAllGarantias() {
        return garantiaRepository.findAll().stream()
                .map(this::toGarantiaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<GarantiaResponse> getGarantiasByFilters(Integer idCliente, String estado, Integer idProducto) {
        return garantiaRepository.findByFilters(idCliente, estado, idProducto).stream()
                .map(this::toGarantiaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GarantiaResponse getGarantiaById(Integer id) {
        Garantia garantia = garantiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Garantía no encontrada"));
        return toGarantiaResponse(garantia);
    }

    @Override
    @Transactional
    public GarantiaResponse actualizarEstado(Integer id, GarantiaUpdateRequest request) {
        Garantia garantia = garantiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Garantía no encontrada"));

        garantia.setEstado(request.getEstado());
        if (request.getObservaciones() != null) {
            garantia.setObservaciones(request.getObservaciones());
        }

        if ("ENTREGADA".equals(request.getEstado()) || "FALLIDA".equals(request.getEstado())) {
            garantia.setFechaResolucion(LocalDateTime.now());
        }

        garantia = garantiaRepository.save(garantia);
        return toGarantiaResponse(garantia);
    }

    @Override
    public List<GarantiaResponse> getGarantiasAsignadasByUsername(String username) {
        var colaborador = colaboradorRepository.findByUsuarioColaborador(username)
                .orElseThrow(() -> new RuntimeException("Colaborador no encontrado"));

        return garantiaRepository.findByColaboradorAsignado_IdColaborador(colaborador.getIdColaborador()).stream()
                .map(this::toGarantiaResponse)
                .collect(Collectors.toList());
    }

    private GarantiaResponse toGarantiaResponse(Garantia garantia) {
        return GarantiaResponse.builder()
                .idGarantia(garantia.getIdGarantia())
                .idVenta(garantia.getVenta() != null ? garantia.getVenta().getIdVenta() : null)
                .idPedido(garantia.getPedido() != null ? garantia.getPedido().getIdPedido() : null)
                .idProducto(garantia.getProducto().getIdProducto())
                .nombreProducto(garantia.getProducto().getNombreProducto())
                .idCliente(garantia.getCliente().getIdCliente())
                .nombreCliente(garantia.getCliente().getNombreCompleto())
                .descripcionProblema(garantia.getDescripcionProblema())
                .fechaCompra(garantia.getFechaCompra())
                .fechaReporte(garantia.getFechaReporte())
                .estado(garantia.getEstado())
                .idColaboradorAsignado(
                        garantia.getColaboradorAsignado() != null ? garantia.getColaboradorAsignado().getIdColaborador()
                                : null)
                .nombreColaboradorAsignado(garantia.getColaboradorAsignado() != null
                        ? garantia.getColaboradorAsignado().getNombreColaborador()
                        : null)
                .observaciones(garantia.getObservaciones())
                .fechaResolucion(garantia.getFechaResolucion())
                .build();
    }
}
