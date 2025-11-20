# API Documentation - Coolbox ERP

## Base URL
```
http://localhost:8080
```

## Autenticación

Todos los endpoints (excepto `/api/auth/login` y `/api/auth/register`) requieren autenticación mediante JWT token en el header:
```
Authorization: Bearer {token}
```

---

## 1. Autenticación (`/api/auth`)

### POST /api/auth/login
Iniciar sesión en el sistema.

**Request:**
```json
{
  "usuarioColaborador": "admin",
  "contraseñaColaborador": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "colaborador": {
    "idColaborador": 1,
    "nombreColaborador": "Admin User",
    "emailColaborador": "admin@coolbox.pe",
    "rol": {
      "idRol": 1,
      "nombreRol": "ADMIN"
    },
    "sucursal": {
      "idSucursal": 1,
      "nombreSucursal": "Coolbox Lima Centro"
    }
  }
}
```

### POST /api/auth/register
Registrar nuevo colaborador (solo ADMIN).

**Request:**
```json
{
  "nombreColaborador": "Juan Pérez",
  "emailColaborador": "juan@coolbox.pe",
  "numeroColaborador": "987654321",
  "usuarioColaborador": "jperez",
  "contraseñaColaborador": "password123",
  "idRol": 3,
  "idSucursal": 1
}
```

**Response:** Igual que login

### POST /api/auth/change-password
Cambiar contraseña del usuario actual.

**Request:**
```json
{
  "contraseñaActual": "password123",
  "contraseñaNueva": "newpassword456"
}
```

**Response:** `200 OK`

### GET /api/auth/me
Obtener información del usuario actual.

**Response:**
```json
{
  "idColaborador": 1,
  "nombreColaborador": "Admin User",
  "emailColaborador": "admin@coolbox.pe",
  "numeroColaborador": "999888777",
  "usuarioColaborador": "admin",
  "rol": {
    "idRol": 1,
    "nombreRol": "ADMIN"
  },
  "sucursal": {
    "idSucursal": 1,
    "nombreSucursal": "Coolbox Lima Centro"
  },
  "activo": true
}
```

---

## 2. Productos (`/api/productos`)

### POST /api/productos
Crear nuevo producto con imagen.

**Request (multipart/form-data):**
- `producto` (JSON string):
```json
{
  "nombreProducto": "Refrigeradora Samsung RT450",
  "marcaProducto": "Samsung",
  "modeloProducto": "RT-450",
  "idCategoria": 1,
  "dimensionesProducto": "180x70x65 cm",
  "especificacionesProducto": "450L, Twin Cooling Plus, Inverter",
  "pesoProducto": 85.5
}
```
- `imagen` (File): Archivo de imagen

**Response:**
```json
{
  "idProducto": 1,
  "nombreProducto": "Refrigeradora Samsung RT450",
  "marcaProducto": "Samsung",
  "modeloProducto": "RT-450",
  "categoria": "Refrigeración",
  "urlImagenProducto": "https://bucket.s3.amazonaws.com/productos/imagen123.jpg",
  "activo": true
}
```

### PUT /api/productos/{id}
Actualizar producto existente.

**Request:** Igual que POST (multipart/form-data)

### DELETE /api/productos/{id}
Eliminar (desactivar) producto.

**Response:** `200 OK`

### GET /api/productos/{id}
Obtener detalles de un producto.

**Response:**
```json
{
  "idProducto": 1,
  "nombreProducto": "Refrigeradora Samsung RT450",
  "marcaProducto": "Samsung",
  "modeloProducto": "RT-450",
  "categoria": {
    "idCategoria": 1,
    "nombreCategoria": "Refrigeración"
  },
  "dimensionesProducto": "180x70x65 cm",
  "especificacionesProducto": "450L, Twin Cooling Plus",
  "pesoProducto": 85.5,
  "urlImagenProducto": "https://...",
  "proveedores": [
    {
      "idProveedor": 1,
      "nombreProveedor": "Distribuidora ABC",
      "precioCompra": 2000.00,
      "stockDisponible": 15
    }
  ],
  "activo": true
}
```

### GET /api/productos
Listar todos los productos (paginado).

**Query Parameters:**
- `page`: Número de página (default: 0)
- `size`: Tamaño de página (default: 20)
- `sort`: Ordenamiento (ej: `nombreProducto,asc`)

**Response:**
```json
{
  "content": [
    {
      "idProducto": 1,
      "nombreProducto": "Refrigeradora Samsung RT450",
      "marcaProducto": "Samsung",
      "modeloProducto": "RT-450",
      "categoria": "Refrigeración",
      "urlImagenProducto": "https://...",
      "activo": true
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalElements": 150,
  "totalPages": 8
}
```

### GET /api/productos/search
Buscar productos por filtros.

**Query Parameters:**
- `nombre`: Nombre del producto (opcional)
- `marca`: Marca del producto (opcional)
- `idCategoria`: ID de categoría (opcional)
- `page`, `size`, `sort`: Paginación

**Response:** Igual que GET /api/productos

### GET /api/productos/categoria/{id}
Obtener productos por categoría.

**Response:**
```json
[
  {
    "idProducto": 1,
    "nombreProducto": "Refrigeradora Samsung RT450",
    "marcaProducto": "Samsung",
    "categoria": "Refrigeración",
    "urlImagenProducto": "https://..."
  }
]
```

### GET /api/productos/marca/{marca}
Obtener productos por marca.

**Response:** Igual que GET /api/productos/categoria/{id}

---

## 3. Categorías (`/api/categorias`)

### GET /api/categorias
Listar todas las categorías.

**Response:**
```json
[
  {
    "idCategoria": 1,
    "nombreCategoria": "Refrigeración",
    "descripcionCategoria": "Refrigeradoras y congeladores"
  },
  {
    "idCategoria": 2,
    "nombreCategoria": "Lavado",
    "descripcionCategoria": "Lavadoras y secadoras"
  }
]
```

---

## 4. Ventas (`/api/ventas`)

### POST /api/ventas
Crear nueva venta.

**Request:**
```json
{
  "idColaborador": 1,
  "idSucursal": 1,
  "metodoPago": "TARJETA",
  "items": [
    {
      "idProducto": 1,
      "idProveedor": 1,
      "cantidad": 2,
      "precioUnitario": 2500.00,
      "descuento": 0.00
    }
  ],
  "cliente": {
    "nombreCliente": "María García",
    "rucCliente": "20123456789",
    "direccionCliente": "Av. Principal 123"
  }
}
```

**Response:**
```json
{
  "idVenta": 1,
  "numeroBoleta": "B001-00000001",
  "fechaVenta": "2024-01-15T10:30:00",
  "colaborador": "Juan Pérez",
  "sucursal": "Coolbox Lima Centro",
  "metodoPago": "TARJETA",
  "subtotal": 5000.00,
  "descuentoTotal": 0.00,
  "total": 5000.00,
  "estado": "COMPLETADA",
  "items": [
    {
      "producto": "Refrigeradora Samsung RT450",
      "cantidad": 2,
      "precioUnitario": 2500.00,
      "descuento": 0.00,
      "subtotal": 5000.00
    }
  ]
}
```

### GET /api/ventas/{id}
Obtener detalles de una venta.

**Response:** Igual que POST /api/ventas

### GET /api/ventas/colaborador/{id}
Obtener ventas por colaborador (paginado).

**Query Parameters:** `page`, `size`, `sort`

**Response:** PageResponse de VentaResponse

### GET /api/ventas/sucursal/{id}
Obtener ventas por sucursal (paginado).

**Response:** PageResponse de VentaResponse

### PUT /api/ventas/{id}/estado
Actualizar estado de una venta.

**Query Parameters:**
- `idEstado`: ID del nuevo estado

**Response:** VentaResponse actualizado

### POST /api/ventas/{id}/cancel
Cancelar una venta.

**Response:** `200 OK`

### GET /api/ventas/filtros
Buscar ventas con filtros.

**Query Parameters:**
- `fechaInicio`: Fecha inicio (ISO 8601)
- `fechaFin`: Fecha fin (ISO 8601)
- `idSucursal`: ID sucursal (opcional)
- `idColaborador`: ID colaborador (opcional)
- `idEstado`: ID estado (opcional)
- `page`, `size`, `sort`: Paginación

**Response:** PageResponse de VentaResponse

---

## 5. Carrito de Compras (`/api/carrito`)

### GET /api/carrito/{idColaborador}
Obtener carrito del colaborador.

**Response:**
```json
{
  "idCarrito": 1,
  "idColaborador": 1,
  "nombreColaborador": "Juan Pérez",
  "items": [
    {
      "idProducto": 1,
      "nombreProducto": "Refrigeradora Samsung RT450",
      "idProveedor": 1,
      "nombreProveedor": "Distribuidora ABC",
      "idSucursal": 1,
      "nombreSucursal": "Coolbox Lima Centro",
      "cantidad": 2,
      "precioUnitario": 2500.00,
      "descuento": 0.00,
      "subtotal": 5000.00
    }
  ],
  "cantidadItems": 2,
  "subtotal": 5000.00,
  "descuentos": 0.00,
  "total": 5000.00
}
```

### POST /api/carrito/{idColaborador}/items
Agregar item al carrito.

**Request:**
```json
{
  "idProducto": 1,
  "idProveedor": 1,
  "idSucursal": 1,
  "cantidad": 2
}
```

**Response:** CarritoFullResponse

### PUT /api/carrito/{idColaborador}/items/{idProducto}
Actualizar cantidad de un item.

**Query Parameters:**
- `idProveedor`: ID del proveedor
- `idSucursal`: ID de la sucursal
- `cantidad`: Nueva cantidad

**Response:** CarritoFullResponse

### DELETE /api/carrito/{idColaborador}/items/{idProducto}
Eliminar item del carrito.

**Query Parameters:**
- `idProveedor`: ID del proveedor
- `idSucursal`: ID de la sucursal

**Response:** CarritoFullResponse

### DELETE /api/carrito/{idColaborador}
Vaciar carrito completo.

**Response:** `200 OK`

---

## 6. Inventario (`/api/inventario`)

### POST /api/inventario/add
Agregar stock a una sucursal.

**Request:**
```json
{
  "idSucursal": 1,
  "idProducto": 1,
  "idProveedor": 1,
  "stockProducto": 50,
  "precioProducto": 2500.00
}
```

**Response:** `200 OK`

### PUT /api/inventario/update
Actualizar stock existente.

**Request:** Igual que POST /api/inventario/add

**Response:** `200 OK`

### POST /api/inventario/transfer
Transferir stock entre sucursales.

**Request:**
```json
{
  "idProducto": 1,
  "idProveedor": 1,
  "idSucursalOrigen": 1,
  "idSucursalDestino": 2,
  "cantidad": 10
}
```

**Response:** `200 OK`

### GET /api/inventario/sucursal/{id}
Obtener inventario de una sucursal.

**Response:**
```json
[
  {
    "idSucursal": 1,
    "nombreSucursal": "Coolbox Lima Centro",
    "direccionSucursal": "Av. Javier Prado 123",
    "idProducto": 1,
    "nombreProducto": "Refrigeradora Samsung RT450",
    "descripcionProducto": "450L, Twin Cooling Plus",
    "idProveedor": 1,
    "nombreProveedor": "Distribuidora ABC",
    "stockProducto": 50,
    "precioProducto": 2500.00
  }
]
```

### GET /api/inventario/producto/{id}
Obtener inventario de un producto en todas las sucursales.

**Response:** Igual que GET /api/inventario/sucursal/{id}

### GET /api/inventario/low-stock
Obtener productos con stock bajo.

**Query Parameters:**
- `threshold`: Umbral de stock bajo (ej: 10)

**Response:** Igual que GET /api/inventario/sucursal/{id}

---

## 7. Sucursales (`/api/sucursales`)

### POST /api/sucursales
Crear nueva sucursal.

**Request:**
```json
{
  "nombreSucursal": "Coolbox Arequipa",
  "direccionSucursal": "Av. Ejército 456",
  "telefonoSucursal": "054-123456",
  "emailSucursal": "arequipa@coolbox.pe",
  "idCiudad": 5
}
```

**Response:**
```json
{
  "idSucursal": 3,
  "nombreSucursal": "Coolbox Arequipa",
  "direccionSucursal": "Av. Ejército 456",
  "telefonoSucursal": "054-123456",
  "emailSucursal": "arequipa@coolbox.pe",
  "ciudad": "Arequipa",
  "departamento": "Arequipa",
  "activo": true
}
```

### PUT /api/sucursales/{id}
Actualizar sucursal.

**Request:** Igual que POST

### DELETE /api/sucursales/{id}
Eliminar (desactivar) sucursal.

**Response:** `200 OK`

### GET /api/sucursales
Listar todas las sucursales.

**Response:**
```json
[
  {
    "idSucursal": 1,
    "nombreSucursal": "Coolbox Lima Centro",
    "direccionSucursal": "Av. Javier Prado 123",
    "telefonoSucursal": "01-1234567",
    "emailSucursal": "lima@coolbox.pe",
    "ciudad": "Lima",
    "departamento": "Lima",
    "activo": true
  }
]
```

### GET /api/sucursales/{id}
Obtener detalles de una sucursal.

**Response:** Igual que item de GET /api/sucursales

---

## 8. Colaboradores (`/api/colaboradores`)

### GET /api/colaboradores
Listar todos los colaboradores.

**Response:**
```json
[
  {
    "idColaborador": 1,
    "nombreColaborador": "Juan Pérez",
    "emailColaborador": "juan@coolbox.pe",
    "numeroColaborador": "987654321",
    "usuarioColaborador": "jperez",
    "rol": {
      "idRol": 3,
      "nombreRol": "VENDEDOR"
    },
    "sucursal": {
      "idSucursal": 1,
      "nombreSucursal": "Coolbox Lima Centro"
    },
    "activo": true
  }
]
```

### GET /api/colaboradores/{id}
Obtener detalles de un colaborador.

**Response:** Igual que item de GET /api/colaboradores

### GET /api/colaboradores/sucursal/{id}
Obtener colaboradores de una sucursal.

**Response:** Igual que GET /api/colaboradores

### PUT /api/colaboradores/{id}
Actualizar colaborador.

**Request:**
```json
{
  "nombreColaborador": "Juan Pérez Actualizado",
  "emailColaborador": "juan.nuevo@coolbox.pe",
  "numeroColaborador": "987654322",
  "idRol": 3,
  "idSucursal": 1
}
```

**Response:** ColaboradorResponse actualizado

### DELETE /api/colaboradores/{id}
Eliminar (desactivar) colaborador.

**Response:** `200 OK`

---

## 9. Proveedores (`/api/proveedores`)

### POST /api/proveedores
Crear nuevo proveedor.

**Request:**
```json
{
  "nombreProveedor": "Distribuidora XYZ",
  "rucProveedor": "20987654321",
  "direccionProveedor": "Av. Industrial 789",
  "telefonoProveedor": "01-9876543",
  "emailProveedor": "ventas@xyz.com"
}
```

**Response:**
```json
{
  "idProveedor": 2,
  "nombreProveedor": "Distribuidora XYZ",
  "rucProveedor": "20987654321",
  "direccionProveedor": "Av. Industrial 789",
  "telefonoProveedor": "01-9876543",
  "emailProveedor": "ventas@xyz.com",
  "activo": true
}
```

### PUT /api/proveedores/{id}
Actualizar proveedor.

**Request:** Igual que POST

### DELETE /api/proveedores/{id}
Eliminar (desactivar) proveedor.

**Response:** `200 OK`

### GET /api/proveedores
Listar todos los proveedores.

**Response:** Array de ProveedorResponse

### GET /api/proveedores/{id}
Obtener detalles de un proveedor.

**Response:** ProveedorResponse

### POST /api/proveedores/asignar-producto
Asignar producto a proveedor.

**Query Parameters:**
- `idProducto`: ID del producto
- `idProveedor`: ID del proveedor
- `precioCompra`: Precio de compra
- `stockInicial`: Stock inicial

**Response:** `200 OK`

---

## 10. Descuentos (`/api/descuentos`)

### POST /api/descuentos
Crear nuevo descuento.

**Request:**
```json
{
  "idProducto": 1,
  "porcentajeDescuento": 15.00,
  "fechaInicio": "2024-01-20T00:00:00",
  "fechaFin": "2024-01-25T23:59:59",
  "descripcionDescuento": "Descuento por aniversario"
}
```

**Response:**
```json
{
  "idDescuento": 1,
  "producto": "Refrigeradora Samsung RT450",
  "porcentajeDescuento": 15.00,
  "fechaInicio": "2024-01-20T00:00:00",
  "fechaFin": "2024-01-25T23:59:59",
  "descripcionDescuento": "Descuento por aniversario",
  "activo": true
}
```

### PUT /api/descuentos/{id}
Actualizar descuento.

**Request:** Igual que POST

### DELETE /api/descuentos/{id}
Eliminar descuento.

**Response:** `200 OK`

### GET /api/descuentos/activos
Listar descuentos activos.

**Response:** Array de DescuentoResponse

### GET /api/descuentos/producto/{id}
Obtener descuento de un producto.

**Response:** DescuentoResponse

---

## 11. Reportes (`/api/reportes`)

### GET /api/reportes/ventas/periodo
Obtener total de ventas por período.

**Query Parameters:**
- `fechaInicio`: Fecha inicio (ISO 8601)
- `fechaFin`: Fecha fin (ISO 8601)

**Response:**
```json
375000.00
```

### GET /api/reportes/ventas/vendedor
Obtener ventas por vendedor.

**Query Parameters:** `fechaInicio`, `fechaFin`

**Response:**
```json
[
  {
    "idColaborador": 1,
    "nombreColaborador": "Juan Pérez",
    "cantidadVentas": 45,
    "montoTotal": 112500.00
  }
]
```

### GET /api/reportes/ventas/sucursal
Obtener ventas por sucursal.

**Query Parameters:** `fechaInicio`, `fechaFin`

**Response:**
```json
[
  {
    "idSucursal": 1,
    "nombreSucursal": "Coolbox Lima Centro",
    "cantidadVentas": 120,
    "montoTotal": 300000.00
  }
]
```

### GET /api/reportes/productos/top
Obtener productos más vendidos.

**Query Parameters:**
- `fechaInicio`: Fecha inicio
- `fechaFin`: Fecha fin
- `limit`: Cantidad de productos (default: 10)

**Response:**
```json
[
  {
    "idProducto": 1,
    "nombreProducto": "Refrigeradora Samsung RT450",
    "cantidadVendida": 45,
    "montoTotal": 112500.00
  }
]
```

### GET /api/reportes/ventas/metodo-pago
Obtener ventas por método de pago.

**Query Parameters:** `fechaInicio`, `fechaFin`

**Response:**
```json
{
  "EFECTIVO": 150000.00,
  "TARJETA": 200000.00,
  "TRANSFERENCIA": 25000.00
}
```

---

## 12. Chat (`/api/chat`)

### POST /api/chat/rooms
Crear nueva sala de chat.

**Request:**
```json
{
  "nombreRoom": "Ventas Lima",
  "tipoRoom": "GRUPO",
  "participantes": [1, 2, 3]
}
```

**Response:**
```json
{
  "idRoom": 1,
  "nombreRoom": "Ventas Lima",
  "tipoRoom": "GRUPO",
  "fechaCreacion": "2024-01-15T10:00:00",
  "participantes": [
    {
      "idColaborador": 1,
      "nombreColaborador": "Juan Pérez"
    }
  ]
}
```

### GET /api/chat/rooms
Obtener salas del colaborador.

**Query Parameters:**
- `idColaborador`: ID del colaborador

**Response:** Array de ChatRoomResponse

### GET /api/chat/rooms/{id}/messages
Obtener mensajes de una sala.

**Query Parameters:**
- `limit`: Cantidad de mensajes (default: 50)

**Response:**
```json
[
  {
    "idMessage": 1,
    "idColaborador": 1,
    "nombreColaborador": "Juan Pérez",
    "mensaje": "Hola equipo",
    "fechaEnvio": "2024-01-15T10:30:00",
    "leido": true
  }
]
```

### POST /api/chat/rooms/{id}/participants
Agregar participante a sala.

**Query Parameters:**
- `idColaborador`: ID del colaborador a agregar

**Response:** `200 OK`

### WebSocket /ws
Conexión WebSocket para chat en tiempo real.

**Conectar:**
```javascript
const socket = new SockJS('http://localhost:8080/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);
    
    // Suscribirse a una sala
    stompClient.subscribe('/topic/chat/' + roomId, function(message) {
        const msg = JSON.parse(message.body);
        console.log('Mensaje recibido:', msg);
    });
});
```

**Enviar mensaje:**
```javascript
stompClient.send('/app/chat/send', {}, JSON.stringify({
    idRoom: 1,
    idColaborador: 1,
    mensaje: 'Hola a todos'
}));
```

---

## 13. Inteligencia Artificial (`/ai`)

> [!IMPORTANT]
> El servicio de IA consulta **ÚNICAMENTE datos del sistema ERP** (productos, ventas, inventario). No busca información en internet. Todas las respuestas se basan en los datos reales almacenados en la base de datos.

### POST /ai/query
Realizar consulta general al asistente de IA con contexto del ERP.

**Funcionamiento:**
- Detecta palabras clave en la consulta ("producto", "inventario", "venta")
- Obtiene datos relevantes del ERP según el contexto
- Envía los datos como contexto a Gemini AI
- Gemini responde basándose SOLO en los datos proporcionados

**Request:**
```json
{
  "query": "¿Cuáles son los productos más vendidos?"
}
```

**Response:**
```json
{
  "contexto": "¿Cuáles son los productos más vendidos?",
  "respuesta": "Basándome en los datos del sistema ERP - COOLBOX:\\n\\nPRODUCTOS ACTIVOS: 45\\n- Refrigeradora Samsung RT450 (Samsung)\\n- Lavadora LG TurboWash (LG)\\n...\\n\\nLos productos más destacados son las refrigeradoras Samsung y las lavadoras LG.",
  "exito": true
}
```

### GET /ai/recommendations/{idColaborador}
Obtener recomendaciones de productos para un colaborador basadas en datos del ERP.

**Datos consultados:**
- Información del colaborador y su sucursal
- Ventas del colaborador (últimos 30 días)
- Productos activos en el sistema
- Inventario disponible

**Response:**
```json
{
  "contexto": "Recomendaciones para colaborador 1",
  "respuesta": "DATOS DEL SISTEMA ERP - COOLBOX:\\n\\nColaborador: Juan Pérez\\nSucursal: Coolbox Lima Centro\\nVentas últimos 30 días: 15\\n\\nRecomendaciones:\\n1. Refrigeradoras Samsung - Alta demanda\\n2. Lavadoras LG - Buen margen\\n3. Cocinas Indurama - Stock disponible",
  "exito": true
}
```

### GET /ai/analysis/{idSucursal}
Analizar datos de ventas e inventario de una sucursal.

**Datos consultados:**
- Ventas de la sucursal (últimos 30 días)
- Inventario actual completo
- Productos con stock bajo (<10 unidades)
- Monto total de ventas

**Response:**
```json
{
  "contexto": "Análisis de sucursal 1",
  "respuesta": "DATOS DEL SISTEMA ERP - COOLBOX:\\n\\nANÁLISIS DE SUCURSAL (Últimos 30 días):\\nTotal de ventas: 45\\nMonto total vendido: S/ 125,450.00\\n\\nINVENTARIO ACTUAL:\\n- Refrigeradora Samsung RT450: 8 unidades - S/ 2,499.00\\n\\nProductos con stock bajo (<10): 12\\n\\nANÁLISIS:\\n1. Rendimiento de ventas: Excelente\\n2. Estado del inventario: 12 productos requieren reabastecimiento\\n3. Recomendaciones: Reabastecer refrigeradoras Samsung",
  "exito": true
}
```

> [!NOTE]
> Todas las respuestas de IA incluyen los datos del ERP consultados para transparencia y verificación.

---

## 14. Roles (`/roles`)

### POST /roles
Crear nuevo rol (solo ADMIN).

**Request:**
```json
{
  "nombreRol": "SUPERVISOR",
  "descripcionRol": "Supervisor de área"
}
```

**Response:**
```json
{
  "idRol": 4,
  "nombreRol": "SUPERVISOR",
  "descripcion": "Supervisor de área"
}
```

### PUT /roles/{id}
Actualizar rol existente (solo ADMIN).

**Request:**
```json
{
  "nombreRol": "SUPERVISOR",
  "descripcionRol": "Supervisor de área actualizado"
}
```

**Response:** RolResponse actualizado

### DELETE /roles/{id}
Eliminar rol (solo ADMIN).

**Response:** `200 OK`

### GET /roles/{id}
Obtener detalles de un rol.

**Response:**
```json
{
  "idRol": 1,
  "nombreRol": "ADMIN",
  "descripcion": "Administrador del sistema"
}
```

### GET /roles
Listar todos los roles.

**Response:**
```json
[
  {
    "idRol": 1,
    "nombreRol": "ADMIN",
    "descripcion": "Administrador del sistema"
  },
  {
    "idRol": 2,
    "nombreRol": "GERENTE",
    "descripcion": "Gerente de sucursal"
  },
  {
    "idRol": 3,
    "nombreRol": "VENDEDOR",
    "descripcion": "Vendedor de tienda"
  }
]
```

---

## 13. Categorías (`/categorias`)

### POST /categorias
Crear nueva categoría (solo ADMIN).

**Request:**
```json
{
  "nombreCategoria": "Cocina",
  "descripcion": "Electrodomésticos para cocina"
}
```

**Response:**
```json
{
  "idCategoria": 5,
  "nombreCategoria": "Cocina",
  "descripcionCategoria": "Electrodomésticos para cocina"
}
```

### PUT /categorias/{id}
Actualizar categoría existente (solo ADMIN).

**Request:**
```json
{
  "nombreCategoria": "Cocina",
  "descripcion": "Electrodomésticos y accesorios para cocina"
}
```

**Response:** CategoriaResponse actualizado

### DELETE /categorias/{id}
Eliminar categoría (solo ADMIN).

**Response:** `200 OK`

### GET /categorias/{id}
Obtener detalles de una categoría.

**Response:**
```json
{
  "idCategoria": 1,
  "nombreCategoria": "Refrigeración",
  "descripcionCategoria": "Refrigeradoras y congeladores"
}
```

### GET /categorias
Listar todas las categorías.

**Response:**
```json
[
  {
    "idCategoria": 1,
    "nombreCategoria": "Refrigeración",
    "descripcionCategoria": "Refrigeradoras y congeladores"
  },
  {
    "idCategoria": 2,
    "nombreCategoria": "Lavado",
    "descripcionCategoria": "Lavadoras y secadoras"
  }
]
```

---

## 15. Departamentos (`/departamentos`)

### POST /departamentos
Crear nuevo departamento (solo ADMIN).

**Request:**
```json
{
  "nombreDepartamento": "Cusco"
}
```

**Response:**
```json
{
  "idDepartamento": 3,
  "nombreDepartamento": "Cusco"
}
```

### PUT /departamentos/{id}
Actualizar departamento existente (solo ADMIN).

**Request:**
```json
{
  "nombreDepartamento": "Cusco Actualizado"
}
```

**Response:** DepartamentoResponse actualizado

### DELETE /departamentos/{id}
Eliminar departamento (solo ADMIN).

**Response:** `200 OK`

### GET /departamentos/{id}
Obtener detalles de un departamento.

**Response:**
```json
{
  "idDepartamento": 1,
  "nombreDepartamento": "Lima"
}
```

### GET /departamentos
Listar todos los departamentos del Perú.

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

---

## 16. Ciudades (`/ciudades`)

### POST /ciudades
Crear nueva ciudad (solo ADMIN).

**Request:**
```json
{
  "nombreCiudad": "Miraflores",
  "idDepartamento": 1
}
```

**Response:**
```json
{
  "idCiudad": 3,
  "nombreCiudad": "Miraflores",
  "idDepartamento": 1,
  "nombreDepartamento": "Lima"
}
```

### PUT /ciudades/{id}
Actualizar ciudad existente (solo ADMIN).

**Request:**
```json
{
  "nombreCiudad": "Miraflores Actualizado",
  "idDepartamento": 1
}
```

**Response:** CiudadResponse actualizado

### DELETE /ciudades/{id}
Eliminar ciudad (solo ADMIN).

**Response:** `200 OK`

### GET /ciudades/{id}
Obtener detalles de una ciudad.

**Response:**
```json
{
  "idCiudad": 1,
  "nombreCiudad": "Lima",
  "idDepartamento": 1,
  "nombreDepartamento": "Lima"
}
```

### GET /ciudades
Listar todas las ciudades.

**Response:**
```json
[
  {
    "idCiudad": 1,
    "nombreCiudad": "Lima",
    "idDepartamento": 1,
    "nombreDepartamento": "Lima"
  },
  {
    "idCiudad": 2,
    "nombreCiudad": "Callao",
    "idDepartamento": 1,
    "nombreDepartamento": "Lima"
  }
]
```

### GET /ciudades/departamento/{id}
Obtener ciudades de un departamento.

**Response:**
```json
[
  {
    "idCiudad": 1,
    "nombreCiudad": "Lima",
    "idDepartamento": 1,
    "nombreDepartamento": "Lima"
  },
  {
    "idCiudad": 2,
    "nombreCiudad": "Callao",
    "idDepartamento": 1,
    "nombreDepartamento": "Lima"
  }
]
```

---

## Códigos de Estado HTTP

- `200 OK`: Solicitud exitosa
- `201 Created`: Recurso creado exitosamente
- `400 Bad Request`: Datos inválidos o falta de parámetros requeridos
- `401 Unauthorized`: No autenticado o token inválido
- `403 Forbidden`: Sin permisos para realizar la acción
- `404 Not Found`: Recurso no encontrado
- `500 Internal Server Error`: Error del servidor

## Manejo de Errores

Todos los errores retornan un objeto con el siguiente formato:

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "El campo nombreProducto es obligatorio",
  "path": "/api/productos"
}
```

## Paginación

Los endpoints que retornan listas paginadas aceptan los siguientes parámetros:

**Query Parameters:**
- `page`: Número de página (0-indexed, default: 0)
- `size`: Tamaño de página (default: 20, max: 100)
- `sort`: Campo y dirección de ordenamiento (ej: `nombreProducto,asc` o `fechaVenta,desc`)

**Formato de respuesta paginada:**
```json
{
  "content": [...],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": true,
      "unsorted": false
    }
  },
  "totalElements": 150,
  "totalPages": 8,
  "last": false,
  "first": true,
  "number": 0,
  "size": 20
}
```

## Formatos de Fecha

Todas las fechas usan el formato ISO 8601:
- **DateTime**: `2024-01-15T10:30:00`
- **Date**: `2024-01-15`

## Notas Importantes

1. **Autenticación**: Todos los endpoints (excepto login y register) requieren JWT token
2. **CORS**: La API acepta peticiones desde cualquier origen (`*`)
3. **Multipart**: Los endpoints de productos que incluyen imágenes usan `multipart/form-data`
4. **WebSocket**: El chat en tiempo real usa STOMP sobre WebSocket en `/ws`
5. **Validación**: Todos los requests son validados automáticamente usando Bean Validation
6. **Transacciones**: Las operaciones de escritura son transaccionales
