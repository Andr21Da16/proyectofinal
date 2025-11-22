# 📚 Documentación Completa de la API - Coolbox ERP

## 🔐 Base URL
```
http://localhost:8080/api
```

## 📋 Tabla de Contenidos
1. [Autenticación](#autenticación)
2. [Productos](#productos)
3. [Categorías](#categorías)
4. [Inventario](#inventario)
5. [Ventas](#ventas)
6. [Clientes](#clientes)
7. [Pedidos](#pedidos)
8. [Carritos](#carritos)
9. [Cupones](#cupones)
10. [Campañas de Marketing](#campañas-de-marketing)
11. [Compras a Proveedores](#compras-a-proveedores)
12. [Proveedores](#proveedores)
13. [Garantías](#garantías)
14. [Colaboradores](#colaboradores)
15. [Roles y Permisos](#roles-y-permisos)
16. [Sucursales](#sucursales)
17. [Ubicaciones](#ubicaciones)
18. [Reportes](#reportes)
19. [Chat Interno](#chat-interno)
20. [Asistente AI](#asistente-ai)

---

## 🔐 Autenticación

### 1. Login de Colaborador (ERP)
**Endpoint:** `POST /auth/login`

**Request Body:**
```json
{
  "usuario": "admin",
  "contraseña": "admin123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "idColaborador": 1,
  "nombreColaborador": "Administrador Principal",
  "usuario": "admin",
  "email": "admin@coolbox.com",
  "rol": {
    "idRol": 1,
    "nombreRol": "Administrador",
    "descripcionRol": "Acceso total al sistema",
    "tipoAcceso": "ERP"
  },
  "sucursal": {
    "idSucursal": 1,
    "nombreSucursal": "Sucursal Lima Centro"
  },
  "permisos": [
    {
      "nombreModulo": "PRODUCTOS",
      "puedeVer": true,
      "puedeEditar": true,
      "puedeCrear": true,
      "puedeEliminar": true
    }
  ]
}
```

### 2. Login de Cliente (Web/Móvil)
**Endpoint:** `POST /cliente-auth/login`

**Request Body:**
```json
{
  "email": "juan.perez@email.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "idCliente": 1,
  "nombreCompleto": "Juan Pérez García",
  "email": "juan.perez@email.com",
  "telefono": "987654321"
}
```

### 3. Registro de Cliente
**Endpoint:** `POST /cliente-auth/register`

**Request Body:**
```json
{
  "nombreCompleto": "Nuevo Cliente Test",
  "email": "nuevo@email.com",
  "password": "password123",
  "telefono": "999888777",
  "direccion": "Av. Test 123",
  "tipoDocumento": "DNI",
  "numeroDocumento": "11223344",
  "idCiudad": 1
}
```

---

## 📦 Productos

### 1. Listar Todos los Productos
**Endpoint:** `GET /productos`

**Response:**
```json
[
  {
    "idProducto": 1,
    "nombreProducto": "Laptop HP Pavilion 15",
    "descripcion": "Laptop con procesador Intel Core i5",
    "marca": "HP",
    "modelo": "Pavilion 15-eh1xxx",
    "categoria": {
      "idCategoria": 1,
      "nombreCategoria": "Laptops"
    },
    "activo": true
  }
]
```

### 2. Buscar Productos
**Endpoint:** `GET /productos/search?nombre=laptop&marca=HP&idCategoria=1`

**Response:**
```json
[
  {
    "idProducto": 1,
    "nombreProducto": "Laptop HP Pavilion 15",
    "marca": "HP",
    "categoria": {
      "nombreCategoria": "Laptops"
    }
  }
]
```

### 3. Obtener Producto por ID
**Endpoint:** `GET /productos/1`

**Response:**
```json
{
  "idProducto": 1,
  "nombreProducto": "Laptop HP Pavilion 15",
  "descripcion": "Laptop con procesador Intel Core i5, 8GB RAM, 256GB SSD",
  "marca": "HP",
  "modelo": "Pavilion 15-eh1xxx",
  "categoria": {
    "idCategoria": 1,
    "nombreCategoria": "Laptops"
  },
  "garantiaMeses": 12,
  "activo": true,
  "sucursales": [
    {
      "idSucursal": 1,
      "nombreSucursal": "Sucursal Lima Centro",
      "stockProducto": 15,
      "precioProducto": 2500.00
    }
  ]
}
```

### 4. Crear Producto
**Endpoint:** `POST /productos`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "nombreProducto": "Mouse Logitech MX Master 3",
  "descripcion": "Mouse inalámbrico ergonómico",
  "marca": "Logitech",
  "modelo": "MX Master 3",
  "idCategoria": 5,
  "garantiaMeses": 24,
  "activo": true
}
```

### 5. Actualizar Producto
**Endpoint:** `PUT /productos/1`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "nombreProducto": "Laptop HP Pavilion 15 (Actualizado)",
  "descripcion": "Laptop con procesador Intel Core i5, 16GB RAM, 512GB SSD",
  "marca": "HP",
  "modelo": "Pavilion 15-eh1xxx",
  "idCategoria": 1,
  "garantiaMeses": 24,
  "activo": true
}
```

### 6. Eliminar Producto (Soft Delete)
**Endpoint:** `DELETE /productos/1`
**Headers:** `Authorization: Bearer {token}`

### 7. Obtener Productos por Sucursal
**Endpoint:** `GET /productos/sucursal/1`

**Response:**
```json
[
  {
    "idProducto": 1,
    "nombreProducto": "Laptop HP Pavilion 15",
    "stockProducto": 15,
    "precioProducto": 2500.00
  }
]
```

### 8. Obtener Todas las Marcas
**Endpoint:** `GET /productos/marcas`

**Response:**
```json
["HP", "Dell", "Lenovo", "Asus", "Acer", "Apple", "Samsung", "LG"]
```

### 9. Obtener Productos de Todas las Sucursales
**Endpoint:** `GET /productos/all-branches`

**Response:**
```json
[
  {
    "idProducto": 1,
    "nombreProducto": "Laptop HP Pavilion 15",
    "marca": "HP",
    "categoria": {
      "nombreCategoria": "Laptops"
    }
  }
]
```

---

## 📂 Categorías

### 1. Listar Todas las Categorías
**Endpoint:** `GET /categorias`

**Response:**
```json
[
  {
    "idCategoria": 1,
    "nombreCategoria": "Laptops",
    "descripcion": "Computadoras portátiles",
    "activo": true
  },
  {
    "idCategoria": 2,
    "nombreCategoria": "Monitores",
    "descripcion": "Pantallas y monitores",
    "activo": true
  }
]
```

### 2. Crear Categoría
**Endpoint:** `POST /categorias`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "nombreCategoria": "Tablets",
  "descripcion": "Tablets y dispositivos móviles",
  "activo": true
}
```

### 3. Actualizar Categoría
**Endpoint:** `PUT /categorias/1`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "nombreCategoria": "Laptops y Notebooks",
  "descripcion": "Computadoras portátiles de todas las marcas",
  "activo": true
}
```

### 4. Eliminar Categoría
**Endpoint:** `DELETE /categorias/1`
**Headers:** `Authorization: Bearer {token}`

---

## 📊 Inventario

### 1. Obtener Inventario por Sucursal
**Endpoint:** `GET /inventario/sucursal/1`
**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
[
  {
    "idProducto": 1,
    "nombreProducto": "Laptop HP Pavilion 15",
    "marca": "HP",
    "stockProducto": 15,
    "precioProducto": 2500.00,
    "nombreProveedor": "Importadora Tech SAC"
  }
]
```

### 2. Actualizar Stock
**Endpoint:** `PUT /inventario/actualizar-stock`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "idSucursal": 1,
  "idProducto": 1,
  "idProveedor": 1,
  "nuevoStock": 20,
  "nuevoPrecio": 2450.00
}
```

### 3. Transferir Stock entre Sucursales
**Endpoint:** `POST /inventario/transferir`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "idSucursalOrigen": 1,
  "idSucursalDestino": 2,
  "idProducto": 1,
  "idProveedor": 1,
  "cantidad": 5
}
```

### 4. Obtener Productos con Stock Bajo
**Endpoint:** `GET /inventario/stock-bajo/1?umbral=10`
**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
[
  {
    "idProducto": 5,
    "nombreProducto": "Teclado Mecánico RGB",
    "stockProducto": 3,
    "precioProducto": 150.00
  }
]
```

---

## 💰 Ventas

### 1. Registrar Venta
**Endpoint:** `POST /ventas`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "idCliente": 1,
  "idColaborador": 1,
  "idSucursal": 1,
  "metodoPago": "TARJETA",
  "idCupon": 1,
  "items": [
    {
      "idProducto": 1,
      "cantidad": 2,
      "precioUnitario": 2500.00
    },
    {
      "idProducto": 5,
      "cantidad": 1,
      "precioUnitario": 150.00
    }
  ]
}
```

**Response:**
```json
{
  "idVenta": 1,
  "numeroVenta": "V-2025-00001",
  "fechaVenta": "2025-01-15T10:30:00",
  "total": 5150.00,
  "descuentoAplicado": 515.00,
  "totalFinal": 4635.00,
  "metodoPago": "TARJETA",
  "cliente": {
    "idCliente": 1,
    "nombreCompleto": "Juan Pérez García"
  },
  "items": [
    {
      "idProducto": 1,
      "nombreProducto": "Laptop HP Pavilion 15",
      "cantidad": 2,
      "precioUnitario": 2500.00,
      "subtotal": 5000.00
    }
  ]
}
```

### 2. Listar Ventas
**Endpoint:** `GET /ventas`
**Headers:** `Authorization: Bearer {token}`

### 3. Obtener Venta por ID
**Endpoint:** `GET /ventas/1`
**Headers:** `Authorization: Bearer {token}`

### 4. Buscar Ventas por Filtros
**Endpoint:** `GET /ventas/buscar?idCliente=1&metodoPago=TARJETA&fechaInicio=2025-01-01&fechaFin=2025-01-31`
**Headers:** `Authorization: Bearer {token}`

### 5. Anular Venta
**Endpoint:** `POST /ventas/1/anular`
**Headers:** `Authorization: Bearer {token}`

---

## 👥 Clientes

### 1. Listar Todos los Clientes
**Endpoint:** `GET /clientes`
**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
[
  {
    "idCliente": 1,
    "nombreCompleto": "Juan Pérez García",
    "email": "juan.perez@email.com",
    "telefono": "987654321",
    "direccion": "Av. Principal 123, Lima",
    "tipoDocumento": "DNI",
    "numeroDocumento": "12345678",
    "ciudad": {
      "idCiudad": 1,
      "nombreCiudad": "Lima"
    },
    "activo": true
  }
]
```

### 2. Buscar Clientes
**Endpoint:** `GET /clientes/buscar?nombre=Juan&documento=12345678`
**Headers:** `Authorization: Bearer {token}`

### 3. Obtener Cliente por ID
**Endpoint:** `GET /clientes/1`
**Headers:** `Authorization: Bearer {token}`

### 4. Crear Cliente
**Endpoint:** `POST /clientes`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "nombreCompleto": "Pedro Gonzales López",
  "email": "pedro.gonzales@email.com",
  "telefono": "999888777",
  "direccion": "Jr. Lima 456",
  "tipoDocumento": "DNI",
  "numeroDocumento": "99887766",
  "idCiudad": 1,
  "password": "password123",
  "activo": true
}
```

### 5. Actualizar Cliente
**Endpoint:** `PUT /clientes/1`
**Headers:** `Authorization: Bearer {token}`

### 6. Eliminar Cliente (Soft Delete)
**Endpoint:** `DELETE /clientes/1`
**Headers:** `Authorization: Bearer {token}`

### 7. Obtener Historial de Compras
**Endpoint:** `GET /clientes/1/historial`
**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
[
  {
    "idVenta": 1,
    "numeroVenta": "V-2025-00001",
    "fechaVenta": "2025-01-15T10:30:00",
    "total": 4635.00,
    "metodoPago": "TARJETA"
  }
]
```

---

## 📦 Pedidos

### 1. Crear Pedido (Cliente Web/Móvil)
**Endpoint:** `POST /pedidos-movil`
**Headers:** `Authorization: Bearer {token_cliente}`

**Request Body:**
```json
{
  "idCliente": 1,
  "direccionEntrega": "Av. Principal 123, Lima",
  "metodoPago": "TARJETA",
  "idSucursal": 1,
  "idCupon": 1,
  "observaciones": "Entregar en horario de oficina",
  "items": [
    {
      "idProducto": 1,
      "cantidad": 1,
      "precioUnitario": 2500.00
    }
  ]
}
```

**Response:**
```json
{
  "idPedido": 1,
  "numeroPedido": "PED-2025-00001",
  "fechaPedido": "2025-01-15T14:30:00",
  "total": 2500.00,
  "descuentoAplicado": 250.00,
  "totalFinal": 2250.00,
  "estadoLogistico": "PENDIENTE",
  "estadoPago": "PENDIENTE",
  "metodoPago": "TARJETA",
  "direccionEntrega": "Av. Principal 123, Lima"
}
```

### 2. Listar Pedidos (ERP)
**Endpoint:** `GET /pedidos`
**Headers:** `Authorization: Bearer {token}`

### 3. Obtener Pedido por ID
**Endpoint:** `GET /pedidos/1`
**Headers:** `Authorization: Bearer {token}`

### 4. Actualizar Estado Logístico
**Endpoint:** `PUT /pedidos/1/estado-logistico`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "nuevoEstado": "EN_PREPARACION"
}
```

**Estados válidos:** `PENDIENTE`, `EN_PREPARACION`, `EN_TRANSITO`, `ENTREGADO`, `CANCELADO`

### 5. Actualizar Estado de Pago
**Endpoint:** `PUT /pedidos/1/estado-pago`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "nuevoEstado": "PAGADO"
}
```

**Estados válidos:** `PENDIENTE`, `PAGADO`, `CONTRAENTREGA`, `DEVUELTO`

### 6. Cancelar Pedido
**Endpoint:** `POST /pedidos/1/cancelar`
**Headers:** `Authorization: Bearer {token}`

### 7. Obtener Pedidos por Cliente (Móvil)
**Endpoint:** `GET /pedidos-movil/cliente/1`
**Headers:** `Authorization: Bearer {token_cliente}`

### 8. Buscar Pedidos por Filtros
**Endpoint:** `GET /pedidos/buscar?estadoLogistico=EN_TRANSITO&estadoPago=PAGADO&idSucursal=1`
**Headers:** `Authorization: Bearer {token}`

---

## 🛒 Carritos

### 1. Obtener Carrito del Cliente
**Endpoint:** `GET /carrito/cliente/1`
**Headers:** `Authorization: Bearer {token_cliente}`

**Response:**
```json
{
  "idCarrito": 1,
  "idCliente": 1,
  "items": [
    {
      "idProducto": 1,
      "nombreProducto": "Laptop HP Pavilion 15",
      "cantidad": 1,
      "precioUnitario": 2500.00,
      "subtotal": 2500.00
    }
  ],
  "total": 2500.00
}
```

### 2. Agregar Producto al Carrito
**Endpoint:** `POST /carrito/agregar`
**Headers:** `Authorization: Bearer {token_cliente}`

**Request Body:**
```json
{
  "idCliente": 1,
  "idProducto": 1,
  "cantidad": 1,
  "idSucursal": 1
}
```

### 3. Actualizar Cantidad
**Endpoint:** `PUT /carrito/actualizar`
**Headers:** `Authorization: Bearer {token_cliente}`

**Request Body:**
```json
{
  "idCliente": 1,
  "idProducto": 1,
  "nuevaCantidad": 2
}
```

### 4. Eliminar Producto del Carrito
**Endpoint:** `DELETE /carrito/eliminar?idCliente=1&idProducto=1`
**Headers:** `Authorization: Bearer {token_cliente}`

### 5. Vaciar Carrito
**Endpoint:** `DELETE /carrito/vaciar/1`
**Headers:** `Authorization: Bearer {token_cliente}`

---

## 🎟️ Cupones

### 1. Listar Todos los Cupones
**Endpoint:** `GET /cupones`
**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
[
  {
    "idCupon": 1,
    "codigo": "BIENVENIDA10",
    "descripcion": "Descuento de bienvenida 10%",
    "descuentoPorcentaje": 10.00,
    "descuentoMonto": null,
    "fechaInicio": "2025-01-01T00:00:00",
    "fechaFin": "2025-12-31T23:59:59",
    "usoMaximo": 100,
    "usoActual": 5,
    "activo": true,
    "montoMinimo": 50.00
  }
]
```

### 2. Validar Cupón
**Endpoint:** `POST /cupones/validar`

**Request Body:**
```json
{
  "codigo": "BIENVENIDA10",
  "montoCompra": 100.00
}
```

**Response:**
```json
{
  "valido": true,
  "idCupon": 1,
  "codigo": "BIENVENIDA10",
  "descuentoPorcentaje": 10.00,
  "descuentoMonto": null,
  "montoDescuento": 10.00,
  "mensaje": "Cupón válido"
}
```

### 3. Crear Cupón
**Endpoint:** `POST /cupones`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "codigo": "VERANO2025",
  "descripcion": "Descuento de verano",
  "descuentoPorcentaje": 15.00,
  "descuentoMonto": null,
  "fechaInicio": "2025-01-01T00:00:00",
  "fechaFin": "2025-03-31T23:59:59",
  "usoMaximo": 200,
  "activo": true,
  "montoMinimo": 100.00
}
```

### 4. Actualizar Cupón
**Endpoint:** `PUT /cupones/1`
**Headers:** `Authorization: Bearer {token}`

### 5. Eliminar Cupón
**Endpoint:** `DELETE /cupones/1`
**Headers:** `Authorization: Bearer {token}`

### 6. Obtener Cupones Activos
**Endpoint:** `GET /cupones/activos`

---

## 📢 Campañas de Marketing

### 1. Listar Todas las Campañas
**Endpoint:** `GET /campanias`
**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
[
  {
    "idCampania": 1,
    "nombreCampania": "Cyber Week 2025",
    "descripcion": "Descuentos especiales en tecnología",
    "fechaInicio": "2025-02-01T00:00:00",
    "fechaFin": "2025-02-07T23:59:59",
    "descuentoPorcentaje": 25.00,
    "activo": true,
    "tipoDescuento": "PORCENTAJE",
    "productos": [
      {
        "idProducto": 1,
        "nombreProducto": "Laptop HP Pavilion 15",
        "precioOriginal": 2500.00,
        "precioConDescuento": 1875.00
      }
    ]
  }
]
```

### 2. Obtener Campañas Activas
**Endpoint:** `GET /campanias/activas`

### 3. Crear Campaña
**Endpoint:** `POST /campanias`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "nombreCampania": "Black Friday 2025",
  "descripcion": "Mega descuentos en toda la tienda",
  "fechaInicio": "2025-11-25T00:00:00",
  "fechaFin": "2025-11-30T23:59:59",
  "descuentoPorcentaje": 40.00,
  "activo": true,
  "tipoDescuento": "PORCENTAJE",
  "idProductos": [1, 2, 3, 5, 8]
}
```

### 4. Actualizar Campaña
**Endpoint:** `PUT /campanias/1`
**Headers:** `Authorization: Bearer {token}`

### 5. Eliminar Campaña (Soft Delete)
**Endpoint:** `DELETE /campanias/1`
**Headers:** `Authorization: Bearer {token}`

### 6. Obtener Campañas por Producto
**Endpoint:** `GET /campanias/producto/1`

---

## 🏭 Compras a Proveedores

### 1. Listar Todas las Compras
**Endpoint:** `GET /compras`
**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
[
  {
    "idCompra": 1,
    "idProveedor": 1,
    "nombreProveedor": "Importadora Tech SAC",
    "fechaCompra": "2025-01-10T09:00:00",
    "total": 50000.00,
    "estado": "RECEPCION_COMPLETA",
    "idColaborador": 1,
    "nombreColaborador": "Administrador Principal",
    "idSucursal": 1,
    "nombreSucursal": "Sucursal Lima Centro",
    "observaciones": "Compra mensual",
    "items": [
      {
        "idProducto": 1,
        "nombreProducto": "Laptop HP Pavilion 15",
        "cantidadSolicitada": 20,
        "cantidadRecibida": 20,
        "precioUnitario": 2500.00,
        "subtotal": 50000.00
      }
    ]
  }
]
```

### 2. Crear Compra
**Endpoint:** `POST /compras`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "idProveedor": 1,
  "idColaborador": 1,
  "idSucursal": 1,
  "observaciones": "Compra de reposición",
  "items": [
    {
      "idProducto": 1,
      "cantidadSolicitada": 10,
      "precioUnitario": 2500.00
    },
    {
      "idProducto": 5,
      "cantidadSolicitada": 50,
      "precioUnitario": 150.00
    }
  ]
}
```

### 3. Actualizar Estado de Compra
**Endpoint:** `PUT /compras/1/estado`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "nuevoEstado": "APROBADA"
}
```

**Estados válidos:** `REGISTRADA`, `APROBADA`, `EN_TRANSITO`, `RECEPCION_PARCIAL`, `RECEPCION_COMPLETA`, `CANCELADA`

### 4. Registrar Recepción de Productos
**Endpoint:** `POST /compras/1/recepcion`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "idProducto": 1,
  "cantidadRecibida": 10
}
```

### 5. Cancelar Compra
**Endpoint:** `POST /compras/1/cancelar`
**Headers:** `Authorization: Bearer {token}`

### 6. Buscar Compras por Filtros
**Endpoint:** `GET /compras/buscar?idProveedor=1&estado=RECEPCION_COMPLETA&idSucursal=1`
**Headers:** `Authorization: Bearer {token}`

---

## 🏢 Proveedores

### 1. Listar Todos los Proveedores
**Endpoint:** `GET /proveedores`
**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
[
  {
    "idProveedor": 1,
    "nombreProveedor": "Importadora Tech SAC",
    "ruc": "20123456789",
    "direccion": "Av. Industrial 1000, Lima",
    "telefono": "014567890",
    "email": "ventas@importadoratech.com",
    "contacto": "Carlos Mendoza",
    "activo": true
  }
]
```

### 2. Crear Proveedor
**Endpoint:** `POST /proveedores`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "nombreProveedor": "Distribuidora Global EIRL",
  "ruc": "20987654321",
  "direccion": "Jr. Comercio 500, Lima",
  "telefono": "019876543",
  "email": "contacto@disglobal.com",
  "contacto": "María Rodríguez",
  "activo": true
}
```

### 3. Actualizar Proveedor
**Endpoint:** `PUT /proveedores/1`
**Headers:** `Authorization: Bearer {token}`

### 4. Eliminar Proveedor (Soft Delete)
**Endpoint:** `DELETE /proveedores/1`
**Headers:** `Authorization: Bearer {token}`

---

## 🛡️ Garantías

### 1. Registrar Garantía
**Endpoint:** `POST /garantias`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "idVenta": 1,
  "idProducto": 1,
  "motivoGarantia": "Pantalla defectuosa",
  "estadoGarantia": "REGISTRADA",
  "observaciones": "Cliente reporta líneas en la pantalla"
}
```

**Response:**
```json
{
  "idGarantia": 1,
  "numeroGarantia": "GAR-2025-00001",
  "fechaRegistro": "2025-01-20T10:00:00",
  "idVenta": 1,
  "numeroVenta": "V-2025-00001",
  "idProducto": 1,
  "nombreProducto": "Laptop HP Pavilion 15",
  "motivoGarantia": "Pantalla defectuosa",
  "estadoGarantia": "REGISTRADA",
  "observaciones": "Cliente reporta líneas en la pantalla"
}
```

### 2. Listar Garantías
**Endpoint:** `GET /garantias`
**Headers:** `Authorization: Bearer {token}`

### 3. Actualizar Estado de Garantía
**Endpoint:** `PUT /garantias/1/estado`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "nuevoEstado": "EN_REVISION"
}
```

**Estados válidos:** `REGISTRADA`, `EN_REVISION`, `APROBADA`, `RECHAZADA`, `RESUELTA`

### 4. Registrar Garantía (Cliente Móvil)
**Endpoint:** `POST /garantias-movil`
**Headers:** `Authorization: Bearer {token_cliente}`

**Request Body:**
```json
{
  "idCliente": 1,
  "idVenta": 1,
  "idProducto": 1,
  "motivoGarantia": "Producto no enciende",
  "descripcionProblema": "El producto dejó de funcionar después de 2 semanas"
}
```

### 5. Obtener Garantías por Cliente
**Endpoint:** `GET /garantias-movil/cliente/1`
**Headers:** `Authorization: Bearer {token_cliente}`

---

## 👨‍💼 Colaboradores

### 1. Listar Todos los Colaboradores
**Endpoint:** `GET /colaboradores`
**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
[
  {
    "idColaborador": 1,
    "nombreColaborador": "Administrador Principal",
    "emailColaborador": "admin@coolbox.com",
    "usuarioColaborador": "admin",
    "rol": {
      "idRol": 1,
      "nombreRol": "Administrador"
    },
    "sucursal": {
      "idSucursal": 1,
      "nombreSucursal": "Sucursal Lima Centro"
    },
    "activo": true
  }
]
```

### 2. Crear Colaborador
**Endpoint:** `POST /colaboradores`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "nombreColaborador": "Juan Vendedor",
  "emailColaborador": "juan.vendedor@coolbox.com",
  "usuarioColaborador": "jvendedor",
  "contraseñaColaborador": "vendedor123",
  "idRol": 2,
  "idSucursal": 1,
  "activo": true
}
```

### 3. Actualizar Colaborador
**Endpoint:** `PUT /colaboradores/1`
**Headers:** `Authorization: Bearer {token}`

### 4. Eliminar Colaborador (Soft Delete)
**Endpoint:** `DELETE /colaboradores/1`
**Headers:** `Authorization: Bearer {token}`

---

## 🔑 Roles y Permisos

### 1. Listar Todos los Roles
**Endpoint:** `GET /roles`
**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
[
  {
    "idRol": 1,
    "nombreRol": "Administrador",
    "descripcionRol": "Acceso total al sistema",
    "tipoAcceso": "ERP",
    "activo": true
  },
  {
    "idRol": 2,
    "nombreRol": "Vendedor",
    "descripcionRol": "Acceso a ventas y clientes",
    "tipoAcceso": "ERP,WEB",
    "activo": true
  }
]
```

### 2. Crear Rol
**Endpoint:** `POST /roles`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "nombreRol": "Supervisor",
  "descripcionRol": "Supervisor de sucursal",
  "tipoAcceso": "ERP",
  "activo": true
}
```

### 3. Obtener Permisos por Rol
**Endpoint:** `GET /rol-permisos/rol/1`
**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
[
  {
    "nombreModulo": "PRODUCTOS",
    "puedeVer": true,
    "puedeEditar": true,
    "puedeCrear": true,
    "puedeEliminar": true
  },
  {
    "nombreModulo": "VENTAS",
    "puedeVer": true,
    "puedeEditar": true,
    "puedeCrear": true,
    "puedeEliminar": false
  }
]
```

### 4. Crear o Actualizar Permiso
**Endpoint:** `POST /rol-permisos`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "idRol": 2,
  "nombreModulo": "INVENTARIO",
  "puedeVer": true,
  "puedeEditar": false,
  "puedeCrear": false,
  "puedeEliminar": false
}
```

### 5. Eliminar Permiso
**Endpoint:** `DELETE /rol-permisos?idRol=2&nombreModulo=INVENTARIO`
**Headers:** `Authorization: Bearer {token}`

---

## 🏪 Sucursales

### 1. Listar Todas las Sucursales
**Endpoint:** `GET /sucursales`

**Response:**
```json
[
  {
    "idSucursal": 1,
    "nombreSucursal": "Sucursal Lima Centro",
    "direccion": "Av. Javier Prado 1500, San Isidro",
    "telefono": "014567890",
    "email": "limacentro@coolbox.com",
    "ciudad": {
      "idCiudad": 1,
      "nombreCiudad": "Lima"
    },
    "activo": true
  }
]
```

### 2. Crear Sucursal
**Endpoint:** `POST /sucursales`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "nombreSucursal": "Sucursal Miraflores",
  "direccion": "Av. Larco 1234, Miraflores",
  "telefono": "014445566",
  "email": "miraflores@coolbox.com",
  "idCiudad": 1,
  "activo": true
}
```

### 3. Actualizar Sucursal
**Endpoint:** `PUT /sucursales/1`
**Headers:** `Authorization: Bearer {token}`

### 4. Eliminar Sucursal (Soft Delete)
**Endpoint:** `DELETE /sucursales/1`
**Headers:** `Authorization: Bearer {token}`

---

## 📍 Ubicaciones

### 1. Listar Departamentos
**Endpoint:** `GET /departamentos`

**Response:**
```json
[
  {
    "idDepartamento": 1,
    "nombreDepartamento": "Lima"
  },
  {
    "idDepartamento": 2,
    "nombreDepartamento": "Arequipa"
  }
]
```

### 2. Listar Ciudades por Departamento
**Endpoint:** `GET /ciudades/departamento/1`

**Response:**
```json
[
  {
    "idCiudad": 1,
    "nombreCiudad": "Lima",
    "departamento": {
      "idDepartamento": 1,
      "nombreDepartamento": "Lima"
    }
  }
]
```

### 3. Crear Departamento
**Endpoint:** `POST /departamentos`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "nombreDepartamento": "Lambayeque"
}
```

### 4. Crear Ciudad
**Endpoint:** `POST /ciudades`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "nombreCiudad": "Chiclayo",
  "idDepartamento": 10
}
```

---

## 📊 Reportes

### 1. Reporte de Ventas por Período
**Endpoint:** `GET /reportes/ventas?fechaInicio=2025-01-01&fechaFin=2025-01-31&idSucursal=1`
**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
{
  "totalVentas": 150000.00,
  "cantidadVentas": 45,
  "promedioVenta": 3333.33,
  "ventasPorDia": [
    {
      "fecha": "2025-01-15",
      "total": 12500.00,
      "cantidad": 5
    }
  ]
}
```

### 2. Productos Más Vendidos
**Endpoint:** `GET /reportes/productos-mas-vendidos?fechaInicio=2025-01-01&fechaFin=2025-01-31&limite=10`
**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
[
  {
    "idProducto": 1,
    "nombreProducto": "Laptop HP Pavilion 15",
    "cantidadVendida": 25,
    "totalVentas": 62500.00
  }
]
```

### 3. Reporte de Inventario
**Endpoint:** `GET /reportes/inventario?idSucursal=1`
**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
{
  "totalProductos": 150,
  "valorTotalInventario": 450000.00,
  "productosBajoStock": 12,
  "productosSinStock": 3
}
```

---

## 💬 Chat Interno

### 1. Listar Salas de Chat
**Endpoint:** `GET /chat/salas`
**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
[
  {
    "idSala": 1,
    "nombreSala": "Equipo de Ventas",
    "descripcion": "Chat del equipo de ventas",
    "activo": true
  }
]
```

### 2. Crear Sala de Chat
**Endpoint:** `POST /chat/salas`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "nombreSala": "Soporte Técnico",
  "descripcion": "Canal de soporte técnico",
  "activo": true
}
```

### 3. Enviar Mensaje
**Endpoint:** `POST /chat/mensajes`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "idSala": 1,
  "idColaborador": 1,
  "mensaje": "Hola equipo, ¿cómo van las ventas hoy?"
}
```

### 4. Obtener Mensajes de una Sala
**Endpoint:** `GET /chat/salas/1/mensajes?limite=50`
**Headers:** `Authorization: Bearer {token}`

**Response:**
```json
[
  {
    "idMensaje": 1,
    "mensaje": "Hola equipo, ¿cómo van las ventas hoy?",
    "fechaEnvio": "2025-01-15T10:30:00",
    "colaborador": {
      "idColaborador": 1,
      "nombreColaborador": "Administrador Principal"
    },
    "leido": true
  }
]
```

### 5. Marcar Mensaje como Leído
**Endpoint:** `PUT /chat/mensajes/1/leer`
**Headers:** `Authorization: Bearer {token}`

---

## 🤖 Asistente AI

### 1. Consultar al Asistente
**Endpoint:** `POST /ai/consulta`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "pregunta": "¿Cuáles son los productos más vendidos este mes?"
}
```

**Response:**
```json
{
  "respuesta": "Los productos más vendidos este mes son:\n1. Laptop HP Pavilion 15 - 25 unidades\n2. Monitor LG 24' - 18 unidades\n3. Teclado Mecánico RGB - 15 unidades",
  "datos": [
    {
      "producto": "Laptop HP Pavilion 15",
      "cantidad": 25
    }
  ]
}
```

### 2. Obtener Recomendaciones de Productos
**Endpoint:** `POST /ai/recomendaciones`
**Headers:** `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "idCliente": 1,
  "contexto": "Cliente interesado en laptops"
}
```

---

## 📝 Notas Importantes

### Autenticación
- Todos los endpoints (excepto login y registro) requieren el header: `Authorization: Bearer {token}`
- Los tokens tienen una duración de 24 horas
- Existen dos tipos de tokens: ERP (colaboradores) y WEB/MÓVIL (clientes)

### Códigos de Estado HTTP
- `200 OK`: Operación exitosa
- `201 Created`: Recurso creado exitosamente
- `400 Bad Request`: Datos inválidos
- `401 Unauthorized`: No autenticado
- `403 Forbidden`: Sin permisos
- `404 Not Found`: Recurso no encontrado
- `500 Internal Server Error`: Error del servidor

### Formatos de Fecha
- Todas las fechas usan formato ISO 8601: `2025-01-15T10:30:00`
- Las fechas sin hora se envían como: `2025-01-15T00:00:00`

### Paginación
Algunos endpoints soportan paginación con parámetros:
- `page`: Número de página (default: 0)
- `size`: Tamaño de página (default: 20)
- `sort`: Campo de ordenamiento

Ejemplo: `GET /productos?page=0&size=10&sort=nombreProducto,asc`

### Datos de Prueba
- **Usuario Admin:** `admin` / `admin123`
- **Usuario Vendedor:** `vendedor` / `vendedor123`
- **Usuario Gerente:** `gerente` / `gerente123`
- **Cliente:** `juan.perez@email.com` / `password123`
- **Cupón de prueba:** `BIENVENIDA10` (10% de descuento)

---

## 🔗 Recursos Adicionales

- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **API Docs:** `http://localhost:8080/v3/api-docs`
- **Health Check:** `http://localhost:8080/actuator/health`

---

**Última actualización:** Enero 2025  
**Versión de la API:** 1.0.0
