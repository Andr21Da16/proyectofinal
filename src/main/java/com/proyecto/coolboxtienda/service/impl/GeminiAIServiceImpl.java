package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.AIQueryRequest;
import com.proyecto.coolboxtienda.dto.response.AIResponse;
import com.proyecto.coolboxtienda.entity.Colaborador;
import com.proyecto.coolboxtienda.entity.Producto;

import com.proyecto.coolboxtienda.entity.Venta;
import com.proyecto.coolboxtienda.entity.SucursalProducto;
import com.proyecto.coolboxtienda.repository.ColaboradorRepository;
import com.proyecto.coolboxtienda.repository.ProductoRepository;

import com.proyecto.coolboxtienda.repository.VentaRepository;
import com.proyecto.coolboxtienda.repository.SucursalProductoRepository;
import com.proyecto.coolboxtienda.service.GeminiAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeminiAIServiceImpl implements GeminiAIService {

    private final WebClient geminiWebClient;
    private final com.proyecto.coolboxtienda.config.GeminiConfig geminiConfig;
    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository;
    private final SucursalProductoRepository sucursalProductoRepository;
    private final ColaboradorRepository colaboradorRepository;

    @Override
    public AIResponse query(AIQueryRequest request) {
        try {
            // Validar permisos del colaborador
            Colaborador colaborador = colaboradorRepository.findById(request.getIdColaborador())
                    .orElseThrow(() -> new RuntimeException("Colaborador no encontrado"));

            // IA LIBERADA: Acceso total al sistema ERP sin restricciones de rol
            // Se ignora la validación de permisos y se entrega todo el contexto

            // Construir contexto completo
            String erpContext = buildFullContext(colaborador);

            // Construir el prompt con contexto del ERP
            String enhancedQuery = buildEnhancedQuery(request.getQuery(), erpContext);

            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> content = new HashMap<>();
            Map<String, String> part = new HashMap<>();
            part.put("text", enhancedQuery);
            content.put("parts", List.of(part));
            requestBody.put("contents", List.of(content));

            String response = geminiWebClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("key", geminiConfig.getApiKey())
                            .build())
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Extraer solo el texto de la respuesta de Gemini
            String cleanResponse = extractTextFromGeminiResponse(response);

            return AIResponse.builder()
                    .contexto(request.getQuery())
                    .respuesta(cleanResponse)
                    .exito(response != null)
                    .build();
        } catch (Exception e) {
            return AIResponse.builder()
                    .contexto(request.getQuery())
                    .respuesta("Error al procesar la consulta: " + e.getMessage())
                    .exito(false)
                    .build();
        }
    }

    @Override
    public AIResponse getProductRecommendations(Integer idColaborador) {
        try {
            // Obtener datos del colaborador y sus ventas
            var colaborador = colaboradorRepository.findById(idColaborador)
                    .orElseThrow(() -> new RuntimeException("Colaborador no encontrado"));

            // IA LIBERADA: Acceso total a recomendaciones

            // Obtener ventas recientes del colaborador (si tiene permiso)
            StringBuilder context = new StringBuilder();
            context.append("DATOS DEL SISTEMA ERP - COOLBOX:\n\n");
            context.append("Colaborador: ").append(colaborador.getNombreColaborador()).append("\n");
            context.append("Sucursal: ").append(colaborador.getSucursal().getNombreSucursal()).append("\n");

            // Incluir ventas siempre
            LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);
            List<Venta> ventasRecientes = ventaRepository.findByColaborador_IdColaboradorAndFechaVentaAfter(
                    idColaborador, hace30Dias);
            context.append("Ventas últimos 30 días: ").append(ventasRecientes.size()).append("\n\n");

            // Obtener productos más vendidos
            List<Producto> productosActivos = productoRepository.findByActivoTrue();
            context.append("PRODUCTOS DISPONIBLES:\n");
            productosActivos.stream().limit(20).forEach(p -> context.append("- ").append(p.getNombreProducto())
                    .append(" (").append(p.getMarcaProducto()).append(")\n"));

            String query = context.toString() +
                    "\n\nCon base en estos datos del sistema ERP, recomienda 5 productos que este colaborador " +
                    "debería promover más, considerando el inventario y tendencias de venta. " +
                    "Responde de forma concisa y profesional.";

            AIQueryRequest request = new AIQueryRequest();
            request.setIdColaborador(idColaborador);
            request.setQuery(query);
            return query(request);
        } catch (Exception e) {
            return AIResponse.builder()
                    .contexto("Recomendaciones para colaborador " + idColaborador)
                    .respuesta("Error: " + e.getMessage())
                    .exito(false)
                    .build();
        }
    }

    @Override
    public AIResponse analyzeSalesData(Integer idSucursal) {
        try {
            // Para análisis de ventas, necesitamos validar que el usuario tenga permisos
            // Este método debería recibir idColaborador también, pero por compatibilidad
            // asumimos que solo administradores pueden acceder a este endpoint

            // Obtener ventas de la sucursal
            LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);
            List<Venta> ventas = ventaRepository.findBySucursal_IdSucursalAndFechaVentaAfter(
                    idSucursal, hace30Dias);

            // Obtener inventario de la sucursal
            List<SucursalProducto> inventario = sucursalProductoRepository
                    .findBySucursal_IdSucursal(idSucursal);

            // Construir contexto con datos reales
            StringBuilder context = new StringBuilder();
            context.append("DATOS DEL SISTEMA ERP - COOLBOX:\n\n");
            context.append("ANÁLISIS DE SUCURSAL (Últimos 30 días):\n");
            context.append("Total de ventas: ").append(ventas.size()).append("\n");

            double totalVentas = ventas.stream()
                    .mapToDouble(v -> v.getTotal().doubleValue())
                    .sum();
            context.append("Monto total vendido: S/ ").append(String.format("%.2f", totalVentas)).append("\n\n");

            context.append("INVENTARIO ACTUAL:\n");
            inventario.stream().limit(15)
                    .forEach(sp -> context.append("- ").append(sp.getProducto().getNombreProducto())
                            .append(": ").append(sp.getStockProducto()).append(" unidades")
                            .append(" - S/ ").append(sp.getPrecioProducto()).append("\n"));

            // Productos con stock bajo
            long productosStockBajo = inventario.stream()
                    .filter(sp -> sp.getStockProducto() < 10)
                    .count();
            context.append("\nProductos con stock bajo (<10): ").append(productosStockBajo).append("\n");

            String query = context.toString() +
                    "\n\nCon base en estos datos reales del sistema ERP, proporciona un análisis breve de:" +
                    "\n1. Rendimiento de ventas" +
                    "\n2. Estado del inventario" +
                    "\n3. Recomendaciones de acción" +
                    "\nResponde de forma concisa y profesional.";

            AIQueryRequest request = new AIQueryRequest();
            request.setIdColaborador(1); // Temporal: asumimos admin
            request.setQuery(query);
            return query(request);
        } catch (Exception e) {
            return AIResponse.builder()
                    .contexto("Análisis de sucursal " + idSucursal)
                    .respuesta("Error: " + e.getMessage())
                    .exito(false)
                    .build();
        }
    }

    /**
     * Construye el contexto completo del ERP sin restricciones de permisos.
     */
    private String buildFullContext(Colaborador colaborador) {
        StringBuilder context = new StringBuilder();
        context.append("DATOS DEL SISTEMA ERP - COOLBOX (ACCESO TOTAL):\n\n");

        // Info Usuario
        context.append("Usuario: ").append(colaborador.getNombreColaborador()).append("\n");
        context.append("Rol: ").append(colaborador.getRol().getNombreRol()).append("\n");
        context.append("Sucursal: ").append(colaborador.getSucursal().getNombreSucursal()).append("\n\n");

        // 1. PRODUCTOS (Top 20)
        List<Producto> productos = productoRepository.findByActivoTrue();
        context.append("PRODUCTOS ACTIVOS (Total: ").append(productos.size()).append("):\n");
        productos.stream().limit(20).forEach(p -> context.append("- ").append(p.getNombreProducto())
                .append(" (").append(p.getMarcaProducto()).append(")\n"));

        // 2. VENTAS (Últimos 30 días)
        LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);
        List<Venta> ventasRecientes = ventaRepository.findByFechaVentaAfter(hace30Dias);
        double totalVentas = ventasRecientes.stream().mapToDouble(v -> v.getTotal().doubleValue()).sum();
        context.append("\nVENTAS GLOBALES (Últimos 30 días):\n");
        context.append("Transacciones: ").append(ventasRecientes.size()).append("\n");
        context.append("Monto total: S/ ").append(String.format("%.2f", totalVentas)).append("\n");

        // 3. INVENTARIO DE LA SUCURSAL
        List<SucursalProducto> inventario = sucursalProductoRepository
                .findBySucursal_IdSucursal(colaborador.getSucursal().getIdSucursal());
        context.append("\nINVENTARIO SUCURSAL ACTUAL:\n");
        inventario.stream().limit(15).forEach(sp -> context.append("- ").append(sp.getProducto().getNombreProducto())
                .append(": ").append(sp.getStockProducto()).append(" unds. - S/ ").append(sp.getPrecioProducto())
                .append("\n"));

        return context.toString();
    }

    private String buildEnhancedQuery(String originalQuery, String erpContext) {
        return erpContext + "\n\nCONSULTA: " + originalQuery +
                "\n\nResponde basándote ÚNICAMENTE en los datos del sistema ERP mostrados arriba. " +
                "No busques información externa. Sé conciso y profesional.";
    }

    private String extractTextFromGeminiResponse(String response) {
        if (response == null)
            return "No se recibió respuesta";

        try {
            // Extraer el texto de la respuesta JSON de Gemini
            // La respuesta tiene formato:
            // {"candidates":[{"content":{"parts":[{"text":"..."}]}}]}
            int textStart = response.indexOf("\"text\"");
            if (textStart == -1)
                return response;

            textStart = response.indexOf(":", textStart) + 1;
            int textEnd = response.indexOf("\"", textStart + 2);

            if (textEnd == -1)
                return response;

            String text = response.substring(textStart + 1, textEnd);
            // Decodificar caracteres escapados
            text = text.replace("\\n", "\n")
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\");

            return text;
        } catch (Exception e) {
            return response;
        }
    }
}
