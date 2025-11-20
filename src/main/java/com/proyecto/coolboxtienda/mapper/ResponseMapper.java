package com.proyecto.coolboxtienda.mapper;

import com.proyecto.coolboxtienda.dto.response.*;
import com.proyecto.coolboxtienda.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResponseMapper {

    // ==================== PAGE MAPPER ====================

    public <T> PageResponse<T> toPageResponse(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .first(page.isFirst())
                .build();
    }

    // ==================== SIMPLE ENTITY MAPPERS ====================

    public DepartamentoResponse toDepartamentoResponse(Departamento departamento) {
        return DepartamentoResponse.builder()
                .idDepartamento(departamento.getIdDepartamento())
                .nombreDepartamento(departamento.getNombreDepartamento())
                .build();
    }

    public CiudadResponse toCiudadResponse(Ciudad ciudad) {
        return CiudadResponse.builder()
                .idCiudad(ciudad.getIdCiudad())
                .nombreCiudad(ciudad.getNombreCiudad())
                .idDepartamento(ciudad.getDepartamento().getIdDepartamento())
                .nombreDepartamento(ciudad.getDepartamento().getNombreDepartamento())
                .build();
    }

    public RolResponse toRolResponse(Rol rol) {
        return RolResponse.builder()
                .idRol(rol.getIdRol())
                .nombreRol(rol.getNombreRol())
                .descripcion(rol.getDescripcionRol())
                .build();
    }

    // ==================== CARRITO DETAIL MAPPER ====================

    public CarritoDetalleResponse toCarritoDetalleResponse(CarritoDetalle detalle) {
        BigDecimal subtotal = detalle.getPrecioUnitario()
                .multiply(BigDecimal.valueOf(detalle.getCantidad()));

        return CarritoDetalleResponse.builder()
                .idProducto(detalle.getProducto().getIdProducto())
                .nombreProducto(detalle.getProducto().getNombreProducto())
                .marcaProducto(detalle.getProducto().getMarcaProducto())
                .urlImagenProducto(detalle.getProducto().getUrlImagenProducto())
                .idProveedor(detalle.getProveedor().getIdProveedor())
                .nombreProveedor(detalle.getProveedor().getNombreProveedor())
                .idSucursal(detalle.getSucursal().getIdSucursal())
                .nombreSucursal(detalle.getSucursal().getNombreSucursal())
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .subtotal(subtotal)
                .build();
    }

    public CarritoFullResponse toCarritoFullResponse(CarritoCompras carrito,
            List<CarritoDetalle> detalles,
            BigDecimal total) {
        List<CarritoDetalleResponse> items = detalles.stream()
                .map(this::toCarritoDetalleResponse)
                .collect(Collectors.toList());

        return CarritoFullResponse.builder()
                .idCarrito(carrito.getIdCarrito())
                .idColaborador(carrito.getColaborador().getIdColaborador())
                .nombreColaborador(carrito.getColaborador().getNombreColaborador())
                .items(items)
                .cantidadItems(detalles.size())
                .subtotal(total)
                .descuentos(BigDecimal.ZERO)
                .total(total)
                .build();
    }

    // ==================== SUCCESS/ERROR MAPPERS ====================

    public SuccessResponse toSuccessResponse(String message, Object data) {
        return SuccessResponse.builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public SuccessResponse toSuccessResponse(String message) {
        return toSuccessResponse(message, null);
    }
}
