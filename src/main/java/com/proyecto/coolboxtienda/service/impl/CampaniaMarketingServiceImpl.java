package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.CampaniaRequest;
import com.proyecto.coolboxtienda.dto.response.CampaniaResponse;
import com.proyecto.coolboxtienda.entity.CampaniaMarketing;
import com.proyecto.coolboxtienda.entity.CampaniaProducto;
import com.proyecto.coolboxtienda.entity.CampaniaProductoId;
import com.proyecto.coolboxtienda.entity.Producto;
import com.proyecto.coolboxtienda.entity.SucursalProducto;
import com.proyecto.coolboxtienda.repository.CampaniaMarketingRepository;
import com.proyecto.coolboxtienda.repository.CampaniaProductoRepository;
import com.proyecto.coolboxtienda.repository.ProductoRepository;
import com.proyecto.coolboxtienda.repository.SucursalProductoRepository;
import com.proyecto.coolboxtienda.service.CampaniaMarketingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaniaMarketingServiceImpl implements CampaniaMarketingService {

    private final CampaniaMarketingRepository campaniaMarketingRepository;
    private final CampaniaProductoRepository campaniaProductoRepository;
    private final ProductoRepository productoRepository;
    private final SucursalProductoRepository sucursalProductoRepository;

    @Override
    public List<CampaniaResponse> getAllCampanias() {
        return campaniaMarketingRepository.findAll().stream()
                .map(this::toCampaniaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CampaniaResponse> getCampaniasActivas() {
        return campaniaMarketingRepository.findActiveCampanias(LocalDateTime.now()).stream()
                .map(this::toCampaniaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CampaniaResponse getCampaniaById(Integer id) {
        CampaniaMarketing campania = campaniaMarketingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaña no encontrada"));
        return toCampaniaResponse(campania);
    }

    @Override
    @Transactional
    public CampaniaResponse createCampania(CampaniaRequest request) {
        CampaniaMarketing campania = new CampaniaMarketing();
        campania.setNombreCampania(request.getNombreCampania());
        campania.setDescripcion(request.getDescripcion());
        campania.setFechaInicio(request.getFechaInicio());
        campania.setFechaFin(request.getFechaFin());
        campania.setDescuentoPorcentaje(request.getDescuentoPorcentaje());
        campania.setActivo(request.getActivo());
        campania.setTipoDescuento(request.getTipoDescuento());

        campania = campaniaMarketingRepository.save(campania);

        // Asociar productos
        for (Integer idProducto : request.getIdProductos()) {
            Producto producto = productoRepository.findById(idProducto)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + idProducto));

            CampaniaProducto cp = new CampaniaProducto();
            cp.setId(new CampaniaProductoId(campania.getIdCampania(), producto.getIdProducto()));
            cp.setCampania(campania);
            cp.setProducto(producto);
            campaniaProductoRepository.save(cp);
        }

        return toCampaniaResponse(campania);
    }

    @Override
    @Transactional
    public CampaniaResponse updateCampania(Integer id, CampaniaRequest request) {
        CampaniaMarketing campania = campaniaMarketingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaña no encontrada"));

        campania.setNombreCampania(request.getNombreCampania());
        campania.setDescripcion(request.getDescripcion());
        campania.setFechaInicio(request.getFechaInicio());
        campania.setFechaFin(request.getFechaFin());
        campania.setDescuentoPorcentaje(request.getDescuentoPorcentaje());
        campania.setActivo(request.getActivo());
        campania.setTipoDescuento(request.getTipoDescuento());

        campania = campaniaMarketingRepository.save(campania);

        // Actualizar productos (eliminar existentes y agregar nuevos)
        campaniaProductoRepository.deleteByCampania_IdCampania(id);
        for (Integer idProducto : request.getIdProductos()) {
            Producto producto = productoRepository.findById(idProducto)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + idProducto));

            CampaniaProducto cp = new CampaniaProducto();
            cp.setId(new CampaniaProductoId(campania.getIdCampania(), producto.getIdProducto()));
            cp.setCampania(campania);
            cp.setProducto(producto);
            campaniaProductoRepository.save(cp);
        }

        return toCampaniaResponse(campania);
    }

    @Override
    @Transactional
    public void deleteCampania(Integer id) {
        CampaniaMarketing campania = campaniaMarketingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaña no encontrada"));
        campania.setActivo(false);
        campaniaMarketingRepository.save(campania);
    }

    @Override
    public List<CampaniaResponse> getCampaniasByProducto(Integer idProducto) {
        List<CampaniaProducto> campaniaProductos = campaniaProductoRepository.findByProducto_IdProducto(idProducto);
        return campaniaProductos.stream()
                .map(cp -> toCampaniaResponse(cp.getCampania()))
                .collect(Collectors.toList());
    }

    private CampaniaResponse toCampaniaResponse(CampaniaMarketing campania) {
        List<CampaniaProducto> campaniaProductos = campaniaProductoRepository
                .findByCampania_IdCampania(campania.getIdCampania());

        List<CampaniaResponse.ProductoCampaniaResponse> productos = campaniaProductos.stream()
                .map(cp -> {
                    Producto p = cp.getProducto();
                    // Obtener precio de la primera sucursal que tenga el producto
                    List<SucursalProducto> sucursales = sucursalProductoRepository
                            .findByProducto_IdProducto(p.getIdProducto());
                    BigDecimal precioOriginal = sucursales.isEmpty() ? BigDecimal.ZERO
                            : sucursales.get(0).getPrecioProducto();

                    BigDecimal descuento = precioOriginal.multiply(campania.getDescuentoPorcentaje())
                            .divide(BigDecimal.valueOf(100));
                    BigDecimal precioConDescuento = precioOriginal.subtract(descuento);

                    return CampaniaResponse.ProductoCampaniaResponse.builder()
                            .idProducto(p.getIdProducto())
                            .nombreProducto(p.getNombreProducto())
                            .precioOriginal(precioOriginal)
                            .precioConDescuento(precioConDescuento)
                            .build();
                })
                .collect(Collectors.toList());

        return CampaniaResponse.builder()
                .idCampania(campania.getIdCampania())
                .nombreCampania(campania.getNombreCampania())
                .descripcion(campania.getDescripcion())
                .fechaInicio(campania.getFechaInicio())
                .fechaFin(campania.getFechaFin())
                .descuentoPorcentaje(campania.getDescuentoPorcentaje())
                .activo(campania.getActivo())
                .tipoDescuento(campania.getTipoDescuento())
                .productos(productos)
                .build();
    }
}
