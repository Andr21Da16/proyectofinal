# Coolbox ERP - Sistema de Gestión Empresarial

API REST completa para el sistema ERP de Coolbox, desarrollada con Spring Boot 3.5.7 y Java 17.

## 📋 Características Principales

- ✅ **Gestión de Ventas Físicas**: Sistema completo de punto de venta con carrito de compras
- ✅ **Gestión de Inventario**: Control de productos por sucursal y proveedor
- ✅ **Gestión de Productos**: CRUD completo con imágenes almacenadas en AWS S3
- ✅ **Sistema de Descuentos**: Descuentos por tiempo y fecha configurables
- ✅ **Múltiples Sucursales**: Gestión de inventario independiente por sucursal
- ✅ **Gestión de Proveedores**: Control de productos y precios por proveedor
- ✅ **Sistema de Usuarios**: Roles (ADMIN, GERENTE, VENDEDOR) con permisos diferenciados
- ✅ **Chat en Tiempo Real**: WebSocket para comunicación entre usuarios
- ✅ **Estado Online**: Visualización de usuarios conectados
- ✅ **Asistente IA**: Integración con Gemini AI para recomendaciones y consultas
- ✅ **Reportes de Ventas**: Reportes por vendedor, sucursal, período, con exportación a Excel
- ✅ **Comprobantes de Venta**: Generación de boletas y facturas
- ✅ **Métodos de Pago**: Yape, Tarjeta, Efectivo
- ✅ **Autenticación JWT**: Seguridad con tokens JWT
- ✅ **Documentación API**: Swagger/OpenAPI integrado
- ✅ **Docker**: Despliegue con Docker Compose

## 🏗️ Arquitectura del Proyecto

```
proyectofinal/
├── src/
│   ├── main/
│   │   ├── java/com/proyecto/coolboxtienda/
│   │   │   ├── config/              # Configuraciones
│   │   │   │   ├── AWSConfig.java
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── WebSocketConfig.java
│   │   │   │   ├── ModelMapperConfig.java
│   │   │   │   └── OpenAPIConfig.java
│   │   │   ├── controller/          # Controladores REST
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── ProductoController.java
│   │   │   │   ├── VentaController.java
│   │   │   │   ├── ReporteController.java
│   │   │   │   ├── ChatController.java
│   │   │   │   ├── AIAssistantController.java
│   │   │   │   ├── SucursalController.java
│   │   │   │   └── CategoriaController.java
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   │   ├── request/
│   │   │   │   └── response/
│   │   │   ├── entity/              # Entidades JPA
│   │   │   │   ├── Producto.java
│   │   │   │   ├── Categoria.java
│   │   │   │   ├── Proveedor.java
│   │   │   │   ├── Sucursal.java
│   │   │   │   ├── Colaborador.java
│   │   │   │   ├── Venta.java
│   │   │   │   ├── CarritoCompras.java
│   │   │   │   ├── Descuento.java
│   │   │   │   ├── ChatRoom.java
│   │   │   │   └── ...
│   │   │   ├── exception/           # Manejo de excepciones
│   │   │   ├── repository/          # Repositorios JPA
│   │   │   ├── security/            # Seguridad JWT
│   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   └── CustomUserDetailsService.java
│   │   │   └── service/             # Lógica de negocio
│   │   │       └── impl/
│   │   └── resources/
│   │       ├── application.yml
│   │       └── db/migration/        # Migraciones Flyway
│   │           ├── V1__initial_schema.sql
│   │           ├── V2__add_descuentos.sql
│   │           ├── V3__add_chat_tables.sql
│   │           └── V4__insert_initial_data.sql
│   └── test/
├── .env.example                     # Plantilla de variables de entorno
├── .gitignore
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

## 🚀 Inicio Rápido

### Prerrequisitos

- Java 17+
- Maven 3.6+
- PostgreSQL 16+ (o usar Docker)
- Cuenta AWS S3 (para almacenamiento de imágenes)
- API Key de Google Gemini (para asistente IA)

### Configuración

1. **Clonar el repositorio**
```bash
git clone <repository-url>
cd proyectofinal
```

2. **Configurar variables de entorno**
```bash
cp .env .env
```

Editar `.env` con tus credenciales:
```env
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/coolbox_erp
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=tu_password

# AWS S3
AWS_ACCESS_KEY=tu_access_key
AWS_SECRET_KEY=tu_secret_key
AWS_BUCKET_NAME=tu_bucket
AWS_REGION=us-east-2

# JWT
JWT_SECRET=tu_secreto_jwt_minimo_256_bits

# Gemini AI
GEMINI_API_KEY=tu_api_key_gemini
```

3. **Ejecutar con Docker Compose** (Recomendado)
```bash
docker-compose up -d
```

La aplicación estará disponible en: `http://localhost:8080/api`

4. **O ejecutar manualmente**
```bash
# Crear base de datos
createdb coolbox_erp

# Compilar y ejecutar
mvn clean install
mvn spring-boot:run
```

## 📚 Documentación de la API

Una vez iniciada la aplicación, accede a:

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api/v3/api-docs

## 🔐 Autenticación

La API utiliza JWT (JSON Web Tokens) para autenticación.

### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "usuario": "admin",
  "contraseña": "password"
}
```

Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "usuario": {
    "id": 1,
    "nombre": "Administrador",
    "rol": "ADMIN"
  }
}
```

### Usar el token
Incluir en el header de todas las peticiones:
```http
Authorization: Bearer {token}
```

## 📊 Endpoints Principales

### Productos
- `GET /api/productos` - Listar todos los productos
- `GET /api/productos/{id}` - Obtener producto por ID
- `POST /api/productos/add` - Crear producto (con imagen)
- `PUT /api/productos/{id}` - Actualizar producto
- `DELETE /api/productos/{id}` - Eliminar producto
- `GET /api/productos/search?q={query}` - Buscar productos

### Ventas
- `POST /api/ventas/carrito/agregar` - Agregar producto al carrito
- `GET /api/ventas/carrito` - Ver carrito actual
- `POST /api/ventas/procesar` - Procesar venta
- `GET /api/ventas/historial` - Historial de ventas
- `GET /api/ventas/{id}/comprobante` - Obtener comprobante

### Reportes
- `GET /api/reportes/ventas?fechaInicio={date}&fechaFin={date}` - Reporte de ventas
- `GET /api/reportes/vendedor/{id}` - Ventas por vendedor
- `GET /api/reportes/sucursal/{id}` - Ventas por sucursal
- `GET /api/reportes/export/excel` - Exportar a Excel

### Chat
- `GET /api/chat/rooms` - Listar salas de chat
- `POST /api/chat/rooms` - Crear sala
- `GET /api/chat/messages/{roomId}` - Obtener mensajes
- `WS /ws` - Conexión WebSocket para chat en tiempo real

### IA Assistant
- `POST /api/ai/query` - Consultar al asistente IA
- `POST /api/ai/recommend` - Obtener recomendaciones de productos

## 🗄️ Base de Datos

El sistema utiliza PostgreSQL con Flyway para migraciones automáticas.

### Tablas Principales
- `departamentos`, `ciudades` - Ubicaciones geográficas
- `categorias` - Categorías de productos
- `productos` - Catálogo de productos
- `proveedores` - Proveedores
- `productos_proveedores` - Relación producto-proveedor con precios
- `sucursales` - Sucursales/tiendas
- `sucursal_producto` - Inventario por sucursal
- `roles`, `colaborador` - Usuarios del sistema
- `carrito_compras`, `carrito_detalle` - Carritos de compra
- `ventas`, `detalle_venta` - Transacciones de venta
- `boletas` - Comprobantes
- `descuentos` - Descuentos por tiempo
- `chat_rooms`, `chat_messages`, `chat_participants` - Sistema de chat
- `chat_online_status` - Estado online de usuarios

## 👥 Roles y Permisos

### ADMIN
- Acceso completo al sistema
- Gestión de usuarios, sucursales, productos
- Visualización de todos los reportes

### GERENTE
- Visualización de todas las sucursales
- Reportes globales
- No puede gestionar usuarios

### VENDEDOR
- Asignado a una sucursal específica
- Realizar ventas
- Ver su historial de ventas
- Chat con otros usuarios

## 🔧 Tecnologías Utilizadas

- **Backend**: Spring Boot 3.5.7
- **Seguridad**: Spring Security + JWT
- **Base de Datos**: PostgreSQL 16
- **ORM**: Spring Data JPA + Hibernate
- **Migraciones**: Flyway
- **Almacenamiento**: AWS S3
- **WebSocket**: Spring WebSocket + STOMP
- **IA**: Google Gemini API
- **Documentación**: SpringDoc OpenAPI (Swagger)
- **Reportes**: Apache POI (Excel)
- **Contenedores**: Docker + Docker Compose

## 📦 Dependencias Principales

```xml
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-security
- spring-boot-starter-websocket
- spring-boot-starter-validation
- postgresql
- flyway-core
- jjwt (JWT)
- aws-sdk-s3
- springdoc-openapi
- modelmapper
- apache-poi (Excel)
```

## 🧪 Testing

```bash
# Ejecutar tests
mvn test

# Ejecutar tests con cobertura
mvn test jacoco:report
```

## 📝 Ejemplos de Uso

### Crear un producto con imagen

```bash
curl -X POST http://localhost:8080/api/productos/add \
  -H "Authorization: Bearer {token}" \
  -F "producto={\"nombreProducto\":\"Refrigeradora LG\",\"marcaProducto\":\"LG\",\"modeloProducto\":\"GT-459\",\"categoria\":\"Refrigeración\"}" \
  -F "file=@imagen.jpg"
```

### Procesar una venta

```bash
curl -X POST http://localhost:8080/api/ventas/procesar \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "metodoPago": "TARJETA",
    "cliente": {
      "nombre": "Juan Pérez",
      "ruc": "12345678901"
    }
  }'
```

## 🐳 Docker

### Construir imagen
```bash
docker build -t coolbox-erp .
```

### Ejecutar con Docker Compose
```bash
docker-compose up -d
```

### Ver logs
```bash
docker-compose logs -f app
```

### Detener servicios
```bash
docker-compose down
```

## 🔒 Seguridad

- Contraseñas hasheadas con BCrypt
- Tokens JWT con expiración
- CORS configurado
- Validación de entrada en todos los endpoints
- Protección contra SQL Injection (JPA)
- Rate limiting (configurar en producción)

## 📈 Monitoreo

La aplicación expone endpoints de Actuator:

- `/actuator/health` - Estado de la aplicación
- `/actuator/info` - Información de la aplicación
- `/actuator/metrics` - Métricas

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia Apache 2.0

## 👨‍💻 Autor

Coolbox Team - [soporte@coolbox.pe](mailto:soporte@coolbox.pe)

## 🆘 Soporte

Para reportar bugs o solicitar features, por favor abre un issue en el repositorio.

---

**Nota**: Recuerda cambiar todas las credenciales de ejemplo antes de desplegar en producción.
