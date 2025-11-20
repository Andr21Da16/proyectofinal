package com.proyecto.coolboxtienda.mapper;

import com.proyecto.coolboxtienda.dto.request.*;
import com.proyecto.coolboxtienda.dto.response.*;
import com.proyecto.coolboxtienda.entity.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityMapper {

    // ==================== PRODUCTO MAPPERS ====================

    public Producto toProductoEntity(ProductoRequest request, Categoria categoria, String urlImagen) {
        Producto producto = new Producto();
        producto.setNombreProducto(request.getNombreProducto());
        producto.setMarcaProducto(request.getMarcaProducto());
        producto.setModeloProducto(request.getModeloProducto());
        producto.setCategoria(categoria);
        producto.setDimensionesProducto(request.getDimensionesProducto());
        producto.setEspecificacionesProducto(request.getEspecificacionesProducto());
        producto.setPesoProducto(request.getPesoProducto());
        producto.setUrlImagenProducto(urlImagen);
        producto.setActivo(true);
        producto.setFechaCreacion(LocalDateTime.now());
        return producto;
    }

    public void updateProductoEntity(Producto producto, ProductoRequest request, Categoria categoria,
            String urlImagen) {
        producto.setNombreProducto(request.getNombreProducto());
        producto.setMarcaProducto(request.getMarcaProducto());
        producto.setModeloProducto(request.getModeloProducto());
        producto.setCategoria(categoria);
        producto.setDimensionesProducto(request.getDimensionesProducto());
        producto.setEspecificacionesProducto(request.getEspecificacionesProducto());
        producto.setPesoProducto(request.getPesoProducto());
        if (urlImagen != null) {
            producto.setUrlImagenProducto(urlImagen);
        }
    }

    public ProductoResponse toProductoResponse(Producto producto) {
        return ProductoResponse.builder()
                .idProducto(producto.getIdProducto())
                .nombreProducto(producto.getNombreProducto())
                .marcaProducto(producto.getMarcaProducto())
                .modeloProducto(producto.getModeloProducto())
                .dimensionesProducto(producto.getDimensionesProducto())
                .especificacionesProducto(producto.getEspecificacionesProducto())
                .pesoProducto(producto.getPesoProducto())
                .urlImagenProducto(producto.getUrlImagenProducto())
                .categoriaNombre(producto.getCategoria().getNombreCategoria())
                .idCategoria(producto.getCategoria().getIdCategoria())
                .activo(producto.getActivo())
                .build();
    }

    public ProductoResponse toProductoResponseWithDiscount(Producto producto, Descuento descuento) {
        ProductoResponse response = toProductoResponse(producto);
        if (descuento != null && descuento.getActivo()
                && descuento.getFechaInicio().isBefore(LocalDateTime.now())
                && descuento.getFechaFin().isAfter(LocalDateTime.now())) {
            response.setPorcentajeDescuento(descuento.getPorcentajeDescuento());
            response.setDescuentoActual(descuento.getPorcentajeDescuento());
        }
        return response;
    }

    public ProductoDetailResponse toProductoDetailResponse(Producto producto,
            List<SucursalProducto> inventario,
            List<ProductoProveedor> proveedores) {
        return ProductoDetailResponse.builder()
                .idProducto(producto.getIdProducto())
                .nombreProducto(producto.getNombreProducto())
                .marcaProducto(producto.getMarcaProducto())
                .modeloProducto(producto.getModeloProducto())
                .dimensionesProducto(producto.getDimensionesProducto())
                .especificacionesProducto(producto.getEspecificacionesProducto())
                .pesoProducto(producto.getPesoProducto())
                .urlImagenProducto(producto.getUrlImagenProducto())
                .categoriaNombre(producto.getCategoria().getNombreCategoria())
                .idCategoria(producto.getCategoria().getIdCategoria())
                .activo(producto.getActivo())
                .inventario(inventario.stream().map(this::toInventarioResponse).collect(Collectors.toList()))
                .proveedores(proveedores.stream().map(this::toProveedorSimpleResponse).collect(Collectors.toList()))
                .build();
    }

    // ==================== COLABORADOR MAPPERS ====================

    public Colaborador toColaboradorEntity(RegisterRequest request, Rol rol, Sucursal sucursal, String hashedPassword) {
        Colaborador colaborador = new Colaborador();
        colaborador.setNombreColaborador(request.getNombreColaborador());
        colaborador.setEmailColaborador(request.getEmailColaborador());
        colaborador.setNumeroColaborador(request.getNumeroColaborador());
        colaborador.setUsuarioColaborador(request.getUsuarioColaborador());
        colaborador.setContraseñaColaborador(hashedPassword);
        colaborador.setRol(rol);
        colaborador.setSucursal(sucursal);
        colaborador.setActivo(true);
        colaborador.setFechaCreacion(LocalDateTime.now());
        return colaborador;
    }

    public void updateColaboradorEntity(Colaborador colaborador, ColaboradorUpdateRequest request,
            Rol rol, Sucursal sucursal) {
        if (request.getNombreColaborador() != null) {
            colaborador.setNombreColaborador(request.getNombreColaborador());
        }
        if (request.getEmailColaborador() != null) {
            colaborador.setEmailColaborador(request.getEmailColaborador());
        }
        if (request.getNumeroColaborador() != null) {
            colaborador.setNumeroColaborador(request.getNumeroColaborador());
        }
        if (request.getUsuarioColaborador() != null) {
            colaborador.setUsuarioColaborador(request.getUsuarioColaborador());
        }
        if (rol != null) {
            colaborador.setRol(rol);
        }
        if (sucursal != null) {
            colaborador.setSucursal(sucursal);
        }
    }

    public ColaboradorResponse toColaboradorResponse(Colaborador colaborador) {
        return ColaboradorResponse.builder()
                .idColaborador(colaborador.getIdColaborador())
                .nombreColaborador(colaborador.getNombreColaborador())
                .emailColaborador(colaborador.getEmailColaborador())
                .numeroColaborador(colaborador.getNumeroColaborador())
                .usuarioColaborador(colaborador.getUsuarioColaborador())
                .rolNombre(colaborador.getRol().getNombreRol())
                .idRol(colaborador.getRol().getIdRol())
                .sucursalNombre(
                        colaborador.getSucursal() != null ? colaborador.getSucursal().getNombreSucursal() : null)
                .idSucursal(colaborador.getSucursal() != null ? colaborador.getSucursal().getIdSucursal() : null)
                .activo(colaborador.getActivo())
                .build();
    }

    // ==================== VENTA MAPPERS ====================

    public VentaResponse toVentaResponse(Venta venta, List<DetalleVenta> detalles) {
        return VentaResponse.builder()
                .idVenta(venta.getIdVenta())
                .fechaVenta(venta.getFechaVenta())
                .totalVenta(venta.getTotal())
                .metodoPago(venta.getMetodoPago())
                .estadoVenta(venta.getEstadoVenta().getNombreEstado())
                .nombreColaborador(venta.getColaborador().getNombreColaborador())
                .nombreSucursal(venta.getSucursal().getNombreSucursal())
                .observaciones(null)
                .detalles(detalles.stream().map(this::toDetalleVentaResponse).collect(Collectors.toList()))
                .build();
    }

    public DetalleVentaResponse toDetalleVentaResponse(DetalleVenta detalle) {
        BigDecimal subtotal = detalle.getPrecioUnitario()
                .multiply(BigDecimal.valueOf(detalle.getCantidad()))
                .subtract(detalle.getDescuento() != null ? detalle.getDescuento() : BigDecimal.ZERO);

        return DetalleVentaResponse.builder()
                .nombreProducto(detalle.getProducto().getNombreProducto())
                .marcaProducto(detalle.getProducto().getMarcaProducto())
                .nombreProveedor(detalle.getProveedor().getNombreProveedor())
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .descuento(detalle.getDescuento())
                .subtotal(subtotal)
                .build();
    }

    // ==================== INVENTARIO MAPPERS ====================

    public InventarioResponse toInventarioResponse(SucursalProducto sp) {
        return InventarioResponse.builder()
                .idSucursal(sp.getSucursal().getIdSucursal())
                .nombreSucursal(sp.getSucursal().getNombreSucursal())
                .idProveedor(sp.getProveedor().getIdProveedor())
                .nombreProveedor(sp.getProveedor().getNombreProveedor())
                .stockProducto(sp.getStockProducto())
                .precioProducto(sp.getPrecioProducto())
                .build();
    }

    // ==================== PROVEEDOR MAPPERS ====================

    public Proveedor toProveedorEntity(ProveedorRequest request, Ciudad ciudad) {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombreProveedor(request.getNombreProveedor());
        proveedor.setEmailProveedor(request.getEmailProveedor());
        proveedor.setTelefonoProveedor(request.getTelefonoProveedor());
        proveedor.setCiudad(ciudad);
        return proveedor;
    }

    public void updateProveedorEntity(Proveedor proveedor, ProveedorRequest request, Ciudad ciudad) {
        proveedor.setNombreProveedor(request.getNombreProveedor());
        proveedor.setEmailProveedor(request.getEmailProveedor());
        proveedor.setTelefonoProveedor(request.getTelefonoProveedor());
        proveedor.setCiudad(ciudad);
    }

    public ProveedorSimpleResponse toProveedorSimpleResponse(ProductoProveedor pp) {
        return ProveedorSimpleResponse.builder()
                .idProveedor(pp.getProveedor().getIdProveedor())
                .nombreProveedor(pp.getProveedor().getNombreProveedor())
                .rucProveedor(null)
                .telefonoProveedor(pp.getProveedor().getTelefonoProveedor())
                .build();
    }

    public ProveedorResponse toProveedorResponse(Proveedor proveedor) {
        return ProveedorResponse.builder()
                .idProveedor(proveedor.getIdProveedor())
                .nombreProveedor(proveedor.getNombreProveedor())
                .rucProveedor(null)
                .direccionProveedor(null)
                .telefonoProveedor(proveedor.getTelefonoProveedor())
                .emailProveedor(proveedor.getEmailProveedor())
                .nombreCiudad(proveedor.getCiudad().getNombreCiudad())
                .nombreDepartamento(proveedor.getCiudad().getDepartamento().getNombreDepartamento())
                .activo(true)
                .build();
    }

    // ==================== SUCURSAL MAPPERS ====================

    public Sucursal toSucursalEntity(SucursalRequest request, Ciudad ciudad) {
        Sucursal sucursal = new Sucursal();
        sucursal.setNombreSucursal(request.getNombreSucursal());
        sucursal.setDireccionSucursal(request.getDireccionSucursal());
        sucursal.setTelefonoSucursal(request.getTelefonoSucursal());
        sucursal.setCiudad(ciudad);
        return sucursal;
    }

    public void updateSucursalEntity(Sucursal sucursal, SucursalRequest request, Ciudad ciudad) {
        sucursal.setNombreSucursal(request.getNombreSucursal());
        sucursal.setDireccionSucursal(request.getDireccionSucursal());
        sucursal.setTelefonoSucursal(request.getTelefonoSucursal());
        sucursal.setCiudad(ciudad);
    }

    public SucursalResponse toSucursalResponse(Sucursal sucursal) {
        return SucursalResponse.builder()
                .idSucursal(sucursal.getIdSucursal())
                .nombreSucursal(sucursal.getNombreSucursal())
                .direccionSucursal(sucursal.getDireccionSucursal())
                .telefonoSucursal(sucursal.getTelefonoSucursal())
                .nombreCiudad(sucursal.getCiudad().getNombreCiudad())
                .nombreDepartamento(sucursal.getCiudad().getDepartamento().getNombreDepartamento())
                .activo(sucursal.getActivo())
                .build();
    }

    // ==================== CATEGORIA MAPPERS ====================

    public Categoria toCategoriaEntity(CategoriaRequest request) {
        Categoria categoria = new Categoria();
        categoria.setNombreCategoria(request.getNombreCategoria());
        categoria.setDescripcionCategoria(request.getDescripcion());
        return categoria;
    }

    public void updateCategoriaEntity(Categoria categoria, CategoriaRequest request) {
        categoria.setNombreCategoria(request.getNombreCategoria());
        categoria.setDescripcionCategoria(request.getDescripcion());
    }

    public CategoriaResponse toCategoriaResponse(Categoria categoria) {
        return CategoriaResponse.builder()
                .idCategoria(categoria.getIdCategoria())
                .nombreCategoria(categoria.getNombreCategoria())
                .descripcion(categoria.getDescripcionCategoria())
                .activo(true)
                .build();
    }

    // ==================== DESCUENTO MAPPERS ====================

    public Descuento toDescuentoEntity(DescuentoRequest request, Producto producto) {
        Descuento descuento = new Descuento();
        descuento.setProducto(producto);
        descuento.setPorcentajeDescuento(request.getPorcentajeDescuento());
        descuento.setFechaInicio(request.getFechaInicio());
        descuento.setFechaFin(request.getFechaFin());
        return descuento;
    }

    public void updateDescuentoEntity(Descuento descuento, DescuentoRequest request) {
        descuento.setPorcentajeDescuento(request.getPorcentajeDescuento());
        descuento.setFechaInicio(request.getFechaInicio());
        descuento.setFechaFin(request.getFechaFin());
    }

    public DescuentoResponse toDescuentoResponse(Descuento descuento) {
        return DescuentoResponse.builder()
                .idDescuento(descuento.getIdDescuento())
                .nombreProducto(descuento.getProducto().getNombreProducto())
                .idProducto(descuento.getProducto().getIdProducto())
                .porcentajeDescuento(descuento.getPorcentajeDescuento())
                .fechaInicio(descuento.getFechaInicio())
                .fechaFin(descuento.getFechaFin())
                .activo(descuento.getActivo())
                .build();
    }

    // ==================== CHAT MAPPERS ====================

    public ChatRoom toChatRoomEntity(ChatRoomRequest request) {
        ChatRoom room = new ChatRoom();
        room.setNombreRoom(request.getNombreRoom());
        room.setTipoRoom(request.getTipoRoom() != null ? request.getTipoRoom() : "GRUPO");
        room.setFechaCreacion(LocalDateTime.now());
        room.setActivo(true);
        return room;
    }

    public ChatMessage toChatMessageEntity(ChatMessageRequest request, ChatRoom room, Colaborador colaborador) {
        ChatMessage message = new ChatMessage();
        message.setRoom(room);
        message.setColaborador(colaborador);
        message.setMensaje(request.getMensaje());
        message.setFechaEnvio(LocalDateTime.now());
        message.setLeido(false);
        return message;
    }

    public ChatMessageResponse toChatMessageResponse(ChatMessage message) {
        return ChatMessageResponse.builder()
                .idMensaje(message.getIdMessage())
                .idRoom(message.getRoom().getIdRoom())
                .nombreColaborador(message.getColaborador().getNombreColaborador())
                .idColaborador(message.getColaborador().getIdColaborador())
                .mensaje(message.getMensaje())
                .fechaEnvio(message.getFechaEnvio())
                .leido(message.getLeido())
                .build();
    }

    public ChatRoomResponse toChatRoomResponse(ChatRoom room, List<ChatParticipant> participants,
            ChatMessage ultimoMensaje, Long mensajesNoLeidos) {
        return ChatRoomResponse.builder()
                .idRoom(room.getIdRoom())
                .nombreRoom(room.getNombreRoom())
                .descripcion(room.getTipoRoom())
                .fechaCreacion(room.getFechaCreacion())
                .activo(room.getActivo())
                .participantes(participants.stream().map(this::toParticipanteResponse).collect(Collectors.toList()))
                .mensajesNoLeidos(mensajesNoLeidos)
                .ultimoMensaje(ultimoMensaje != null ? toChatMessageResponse(ultimoMensaje) : null)
                .build();
    }

    public ParticipanteResponse toParticipanteResponse(ChatParticipant participant) {
        return ParticipanteResponse.builder()
                .idColaborador(participant.getColaborador().getIdColaborador())
                .nombreColaborador(participant.getColaborador().getNombreColaborador())
                .activo(participant.getActivo())
                .build();
    }

    // ==================== COMPROBANTE MAPPERS ====================

    public ComprobanteResponse toComprobanteResponse(Boleta boleta, VentaResponse ventaResponse) {
        return ComprobanteResponse.builder()
                .idBoleta(boleta.getIdBoleta())
                .numeroBoleta(boleta.getNumeroBoleta())
                .fechaEmision(boleta.getFechaEmision())
                .tipoComprobante(boleta.getTipoComprobante())
                .venta(ventaResponse)
                .build();
    }

    // ==================== CARRITO MAPPERS ====================

    public CarritoResponse toCarritoResponse(CarritoCompras carrito, Integer cantidadItems, BigDecimal total) {
        return CarritoResponse.builder()
                .idCarrito(carrito.getIdCarrito())
                .idColaborador(carrito.getColaborador().getIdColaborador())
                .nombreColaborador(carrito.getColaborador().getNombreColaborador())
                .cantidadItems(cantidadItems)
                .total(total)
                .build();
    }
}
