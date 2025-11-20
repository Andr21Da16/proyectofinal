# Estructura del Proyecto Coolbox ERP

## Esquema de Carpetas

```
proyectofinal/
│
├── src/
│   ├── main/
│   │   ├── java/com/proyecto/coolboxtienda/
│   │   │   │
│   │   │   ├── config/                          # Configuraciones de la aplicación
│   │   │   │   ├── AWSConfig.java               # Configuración AWS S3
│   │   │   │   ├── SecurityConfig.java          # Configuración de seguridad JWT
│   │   │   │   ├── WebSocketConfig.java         # Configuración WebSocket para chat
│   │   │   │   ├── ModelMapperConfig.java       # Bean de ModelMapper
│   │   │   │   └── OpenAPIConfig.java           # Configuración Swagger/OpenAPI
│   │   │   │
│   │   │   ├── controller/                      # Controladores REST
│   │   │   │   ├── AuthController.java          # Login, registro, refresh token
│   │   │   │   ├── ProductoController.java      # CRUD productos
│   │   │   │   ├── VentaController.java         # Gestión de ventas y carrito
│   │   │   │   ├── ReporteController.java       # Reportes y exportación
│   │   │   │   ├── ChatController.java          # API REST para chat
│   │   │   │   ├── ChatWebSocketController.java # WebSocket para mensajes en tiempo real
│   │   │   │   ├── AIAssistantController.java   # Consultas a Gemini IA
│   │   │   │   ├── SucursalController.java      # CRUD sucursales
│   │   │   │   ├── CategoriaController.java     # CRUD categorías
│   │   │   │   ├── ProveedorController.java     # CRUD proveedores
│   │   │   │   ├── DescuentoController.java     # Gestión de descuentos
│   │   │   │   └── ColaboradorController.java   # Gestión de usuarios
│   │   │   │
│   │   │   ├── dto/                             # Data Transfer Objects
│   │   │   │   ├── request/                     # DTOs de entrada
│   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   ├── RegisterRequest.java
│   │   │   │   │   ├── ProductoRequest.java
│   │   │   │   │   ├── VentaRequest.java
│   │   │   │   │   ├── CarritoItemRequest.java
│   │   │   │   │   ├── ChatMessageRequest.java
│   │   │   │   │   ├── AIQueryRequest.java
│   │   │   │   │   └── DescuentoRequest.java
│   │   │   │   │
│   │   │   │   └── response/                    # DTOs de salida
│   │   │   │       ├── AuthResponse.java
│   │   │   │       ├── ProductoResponse.java
│   │   │   │       ├── VentaResponse.java
│   │   │   │       ├── ComprobanteResponse.java
│   │   │   │       ├── ReporteResponse.java
│   │   │   │       ├── ChatMessageResponse.java
│   │   │   │       └── AIResponse.java
│   │   │   │
│   │   │   ├── entity/                          # Entidades JPA
│   │   │   │   ├── Departamento.java
│   │   │   │   ├── Ciudad.java
│   │   │   │   ├── Categoria.java
│   │   │   │   ├── Producto.java
│   │   │   │   ├── Proveedor.java
│   │   │   │   ├── ProductoProveedor.java
│   │   │   │   ├── Sucursal.java
│   │   │   │   ├── SucursalProducto.java
│   │   │   │   ├── Rol.java
│   │   │   │   ├── Colaborador.java
│   │   │   │   ├── Descuento.java
│   │   │   │   ├── CarritoCompras.java
│   │   │   │   ├── CarritoDetalle.java
│   │   │   │   ├── EstadoVenta.java
│   │   │   │   ├── Venta.java
│   │   │   │   ├── DetalleVenta.java
│   │   │   │   ├── Boleta.java
│   │   │   │   ├── ChatRoom.java
│   │   │   │   ├── ChatParticipant.java
│   │   │   │   ├── ChatMessage.java
│   │   │   │   └── ChatOnlineStatus.java
│   │   │   │
│   │   │   ├── exception/                       # Manejo de excepciones
│   │   │   │   ├── GlobalExceptionHandler.java  # Handler global
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── BusinessException.java
│   │   │   │   ├── UnauthorizedException.java
│   │   │   │   └── ErrorResponse.java           # DTO de error
│   │   │   │
│   │   │   ├── repository/                      # Repositorios JPA
│   │   │   │   ├── DepartamentoRepository.java
│   │   │   │   ├── CiudadRepository.java
│   │   │   │   ├── CategoriaRepository.java
│   │   │   │   ├── ProductoRepository.java
│   │   │   │   ├── ProveedorRepository.java
│   │   │   │   ├── ProductoProveedorRepository.java
│   │   │   │   ├── SucursalRepository.java
│   │   │   │   ├── SucursalProductoRepository.java
│   │   │   │   ├── RolRepository.java
│   │   │   │   ├── ColaboradorRepository.java
│   │   │   │   ├── DescuentoRepository.java
│   │   │   │   ├── CarritoComprasRepository.java
│   │   │   │   ├── CarritoDetalleRepository.java
│   │   │   │   ├── EstadoVentaRepository.java
│   │   │   │   ├── VentaRepository.java
│   │   │   │   ├── DetalleVentaRepository.java
│   │   │   │   ├── BoletaRepository.java
│   │   │   │   ├── ChatRoomRepository.java
│   │   │   │   ├── ChatParticipantRepository.java
│   │   │   │   ├── ChatMessageRepository.java
│   │   │   │   └── ChatOnlineStatusRepository.java
│   │   │   │
│   │   │   ├── security/                        # Seguridad JWT
│   │   │   │   ├── JwtTokenProvider.java        # Generación y validación de tokens
│   │   │   │   ├── JwtAuthenticationFilter.java # Filtro de autenticación
│   │   │   │   └── CustomUserDetailsService.java # Carga de usuarios
│   │   │   │
│   │   │   ├── service/                         # Interfaces de servicios
│   │   │   │   ├── FileUploadService.java
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── ProductoService.java
│   │   │   │   ├── VentaService.java
│   │   │   │   ├── ReporteService.java
│   │   │   │   ├── ChatService.java
│   │   │   │   ├── GeminiAIService.java
│   │   │   │   ├── SucursalService.java
│   │   │   │   ├── CategoriaService.java
│   │   │   │   ├── ProveedorService.java
│   │   │   │   ├── DescuentoService.java
│   │   │   │   └── ColaboradorService.java
│   │   │   │
│   │   │   └── service/impl/                    # Implementaciones de servicios
│   │   │       ├── FileUploadServiceImpl.java
│   │   │       ├── AuthServiceImpl.java
│   │   │       ├── ProductoServiceImpl.java
│   │   │       ├── VentaServiceImpl.java
│   │   │       ├── ReporteServiceImpl.java
│   │   │       ├── ChatServiceImpl.java
│   │   │       ├── GeminiAIServiceImpl.java
│   │   │       ├── SucursalServiceImpl.java
│   │   │       ├── CategoriaServiceImpl.java
│   │   │       ├── ProveedorServiceImpl.java
│   │   │       ├── DescuentoServiceImpl.java
│   │   │       └── ColaboradorServiceImpl.java
│   │   │
│   │   └── resources/
│   │       ├── application.yml                  # Configuración principal
│   │       │
│   │       └── db/migration/                    # Migraciones Flyway
│   │           ├── V1__initial_schema.sql       # Esquema inicial
│   │           ├── V2__add_descuentos.sql       # Tabla de descuentos
│   │           ├── V3__add_chat_tables.sql      # Tablas de chat
│   │           └── V4__insert_initial_data.sql  # Datos iniciales
│   │
│   └── test/                                    # Tests
│       └── java/com/proyecto/coolboxtienda/
│           ├── controller/                      # Tests de controladores
│           ├── service/                         # Tests de servicios
│           └── repository/                      # Tests de repositorios
│
├── .env.example                                 # Plantilla de variables de entorno
├── .gitignore                                   # Archivos ignorados por Git
├── docker-compose.yml                           # Configuración Docker Compose
├── Dockerfile                                   # Imagen Docker de la aplicación
├── pom.xml                                      # Dependencias Maven
├── README.md                                    # Documentación principal
└── API_DOCUMENTATION.md                         # Documentación de la API
```

## Descripción de Componentes

### Config
Configuraciones de Spring Boot para diferentes aspectos del sistema:
- AWS S3 para almacenamiento de imágenes
- Seguridad con JWT
- WebSocket para chat en tiempo real
- ModelMapper para conversión de DTOs
- OpenAPI/Swagger para documentación

### Controllers
Endpoints REST organizados por funcionalidad:
- **Auth**: Autenticación y autorización
- **Producto**: Gestión de catálogo
- **Venta**: Proceso de ventas y carrito
- **Reporte**: Generación de reportes
- **Chat**: Mensajería en tiempo real
- **AI**: Asistente inteligente

### DTOs
Objetos de transferencia de datos separados en:
- **Request**: Validación de entrada
- **Response**: Formato de salida (sin datos sensibles)

### Entities
Entidades JPA mapeadas a tablas de PostgreSQL con relaciones:
- OneToMany, ManyToOne, ManyToMany
- Composite keys para tablas pivot
- Campos calculados (GENERATED ALWAYS AS)

### Repositories
Acceso a datos con Spring Data JPA:
- Queries personalizadas con @Query
- Métodos derivados del nombre
- Proyecciones para reportes

### Security
Implementación de seguridad:
- Generación y validación de JWT
- Filtros de autenticación
- Carga de usuarios desde BD

### Services
Lógica de negocio separada en interfaces e implementaciones:
- Transacciones con @Transactional
- Validaciones de negocio
- Integración con servicios externos

## Flujo de Datos

```
Cliente → Controller → Service → Repository → Database
                ↓         ↓
              DTO ←→ Entity (ModelMapper)
```

## Base de Datos

### Esquema SQL
Las migraciones Flyway crean automáticamente:
1. Tablas de ubicación (departamentos, ciudades)
2. Catálogo (categorías, productos, proveedores)
3. Inventario (sucursales, stock)
4. Usuarios (roles, colaboradores)
5. Ventas (carritos, ventas, boletas)
6. Descuentos
7. Chat (salas, mensajes, participantes)

### Relaciones Principales
- Producto → Categoría (ManyToOne)
- Producto ↔ Proveedor (ManyToMany via ProductoProveedor)
- Sucursal → Ciudad (ManyToOne)
- SucursalProducto → Sucursal, Producto, Proveedor (Composite)
- Venta → Colaborador, Sucursal, EstadoVenta (ManyToOne)
- ChatMessage → ChatRoom, Colaborador (ManyToOne)

## Tecnologías por Capa

### Presentación (Controllers)
- Spring Web MVC
- Spring WebSocket
- Bean Validation

### Negocio (Services)
- Spring Service
- Spring Transaction Management
- ModelMapper

### Persistencia (Repositories)
- Spring Data JPA
- Hibernate
- Flyway

### Seguridad
- Spring Security
- JWT (jjwt)
- BCrypt

### Infraestructura
- AWS SDK (S3)
- WebFlux (HTTP Client para Gemini)
- Apache POI (Excel)

## Patrones de Diseño

- **DTO Pattern**: Separación de entidades y objetos de transferencia
- **Repository Pattern**: Abstracción de acceso a datos
- **Service Layer Pattern**: Lógica de negocio centralizada
- **Dependency Injection**: Inyección de dependencias con Spring
- **Builder Pattern**: Construcción de objetos complejos (Lombok)
