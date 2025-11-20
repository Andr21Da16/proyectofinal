# Guía de Inicio Rápido - Coolbox ERP

## 🚀 Configuración Inicial (5 minutos)

### 1. Clonar y Configurar

```bash
# Clonar el repositorio
cd proyectofinal

# Copiar archivo de variables de entorno
cp .env .env
```

### 2. Editar `.env` con tus credenciales

```env
# Base de Datos (dejar así para Docker)
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/coolbox_erp
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

# AWS S3 (REQUERIDO - Obtener de AWS Console)
AWS_ACCESS_KEY=AKIAIOSFODNN7EXAMPLE
AWS_SECRET_KEY=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
AWS_BUCKET_NAME=coolbox-productos
AWS_REGION=us-east-2

# JWT (Generar secreto seguro)
JWT_SECRET=MiSuperSecretoSeguroDeAlMenos256BitsParaProduccion2024

# Gemini AI (REQUERIDO - Obtener de Google AI Studio)
GEMINI_API_KEY=AIzaSyD-9tSrke72PouQMnMX-a7eZSW0jkFMBWY
```

### 3. Iniciar con Docker

```bash
docker-compose up -d
```

¡Listo! La aplicación estará en: http://localhost:8080/api

## 📖 Acceso a Documentación

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **Health Check**: http://localhost:8080/actuator/health

## 🔑 Credenciales Iniciales

El sistema crea automáticamente usuarios de prueba:

```
Admin:
  Usuario: admin
  Contraseña: admin123
  Rol: ADMIN

Gerente:
  Usuario: gerente
  Contraseña: gerente123
  Rol: GERENTE

Vendedor:
  Usuario: vendedor
  Contraseña: vendedor123
  Rol: VENDEDOR
```

## 🧪 Probar la API

### 1. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usuario": "admin",
    "contraseña": "admin123"
  }'
```

Copia el `token` de la respuesta.

### 2. Listar Productos
```bash
curl -X GET http://localhost:8080/api/productos \
  -H "Authorization: Bearer TU_TOKEN_AQUI"
```

### 3. Crear Producto con Imagen
```bash
curl -X POST http://localhost:8080/api/productos/add \
  -H "Authorization: Bearer TU_TOKEN_AQUI" \
  -F 'producto={"nombreProducto":"Refrigeradora LG","marcaProducto":"LG","modeloProducto":"GT-500","categoria":"Refrigeración"}' \
  -F 'file=@/ruta/a/imagen.jpg'
```

## 🛠️ Comandos Útiles

### Ver logs
```bash
docker-compose logs -f app
```

### Reiniciar aplicación
```bash
docker-compose restart app
```

### Detener todo
```bash
docker-compose down
```

### Limpiar y reiniciar
```bash
docker-compose down -v
docker-compose up -d
```

### Acceder a PostgreSQL
```bash
docker-compose exec postgres psql -U postgres -d coolbox_erp
```

## 📱 Probar Chat en Tiempo Real

### Conectar WebSocket (JavaScript)
```javascript
const socket = new SockJS('http://localhost:8080/api/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    console.log('Conectado: ' + frame);
    
    // Suscribirse a sala de chat
    stompClient.subscribe('/topic/chat/1', function(message) {
        console.log('Mensaje recibido:', JSON.parse(message.body));
    });
    
    // Enviar mensaje
    stompClient.send('/app/chat/send', {}, JSON.stringify({
        idRoom: 1,
        mensaje: 'Hola desde WebSocket!'
    }));
});
```

## 🤖 Probar Asistente IA

```bash
curl -X POST http://localhost:8080/api/ai/query \
  -H "Authorization: Bearer TU_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{
    "query": "¿Cuáles son los productos más vendidos?",
    "contexto": "ventas"
  }'
```

## 📊 Generar Reporte

```bash
curl -X GET "http://localhost:8080/api/reportes/ventas?fechaInicio=2024-01-01T00:00:00&fechaFin=2024-12-31T23:59:59" \
  -H "Authorization: Bearer TU_TOKEN_AQUI"
```

## ⚠️ Solución de Problemas

### Error: "Connection refused" a PostgreSQL
```bash
# Verificar que PostgreSQL está corriendo
docker-compose ps

# Reiniciar servicios
docker-compose restart
```

### Error: "Access Denied" en S3
- Verificar que AWS_ACCESS_KEY y AWS_SECRET_KEY son correctos
- Verificar que el bucket existe y tienes permisos
- Verificar la región del bucket

### Error: "Invalid JWT"
- Verificar que el token no ha expirado (1 hora)
- Usar el endpoint /auth/refresh para renovar

### Error de Flyway Migration
```bash
# Limpiar base de datos y reiniciar
docker-compose down -v
docker-compose up -d
```

## 📚 Siguientes Pasos

1. ✅ Configurar credenciales reales de AWS S3
2. ✅ Obtener API Key de Gemini AI
3. ✅ Cambiar contraseñas de usuarios por defecto
4. ✅ Configurar CORS para tu dominio en producción
5. ✅ Revisar documentación completa en README.md
6. ✅ Explorar API en Swagger UI

## 🎯 Flujo de Trabajo Típico

1. **Login** → Obtener token JWT
2. **Crear Productos** → Subir con imágenes
3. **Agregar al Carrito** → Seleccionar productos
4. **Procesar Venta** → Generar comprobante
5. **Ver Reportes** → Analizar ventas
6. **Chat** → Comunicarse con equipo
7. **IA** → Obtener recomendaciones

## 💡 Tips

- Usa Postman o Insomnia para probar la API más fácilmente
- El token JWT expira en 1 hora, usa refresh token
- Las imágenes se suben a S3 automáticamente
- Los descuentos se aplican automáticamente si están activos
- El chat funciona en tiempo real con WebSocket
- La IA puede responder preguntas sobre el inventario y ventas

---

**¿Necesitas ayuda?** Revisa la documentación completa en:
- README.md
- API_DOCUMENTATION.md
- PROJECT_STRUCTURE.md
