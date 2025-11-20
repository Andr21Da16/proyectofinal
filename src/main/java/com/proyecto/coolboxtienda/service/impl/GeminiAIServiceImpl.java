package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.AIQueryRequest;
import com.proyecto.coolboxtienda.dto.response.AIResponse;
import com.proyecto.coolboxtienda.entity.Producto;
import com.proyecto.coolboxtienda.entity.Venta;
import com.proyecto.coolboxtienda.entity.SucursalProducto;
import com.proyecto.coolboxtienda.repository.ProductoRepository;
import com.proyecto.coolboxtienda.repository.VentaRepository;
import com.proyecto.coolboxtienda.repository.SucursalProductoRepository;
import com.proyecto.coolboxtienda.repository.ColaboradorRepository;
import com.proyecto.coolboxtienda.service.GeminiAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            // Obtener datos relevantes del ERP según el contexto de la consulta
            String erpContext = buildERPContext(request.getQuery());

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

            // Obtener ventas recientes del colaborador
            LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);
            List<Venta> ventasRecientes = ventaRepository.findByColaborador_IdColaboradorAndFechaVentaAfter(
                    idColaborador, hace30Dias);

            // Obtener productos más vendidos
            List<Producto> productosActivos = productoRepository.findByActivoTrue();

            // Construir contexto
            StringBuilder context = new StringBuilder();
            context.append("DATOS DEL SISTEMA ERP - COOLBOX:\n\n");
            context.append("Colaborador: ").append(colaborador.getNombreColaborador()).append("\n");
            context.append("Sucursal: ").append(colaborador.getSucursal().getNombreSucursal()).append("\n");
            context.append("Ventas últimos 30 días: ").append(ventasRecientes.size()).append("\n\n");

            context.append("PRODUCTOS DISPONIBLES:\n");
            productosActivos.stream().limit(20).forEach(p -> context.append("- ").append(p.getNombreProducto())
                    .append(" (").append(p.getMarcaProducto()).append(")\n"));

            String query = context.toString() +
                    "\n\nCon base en estos datos del sistema ERP, recomienda 5 productos que este colaborador " +
                    "debería promover más, considerando el inventario y tendencias de venta. " +
                    "Responde de forma concisa y profesional.";

            AIQueryRequest request = new AIQueryRequest();
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

    private String buildERPContext(String query) {
        StringBuilder context = new StringBuilder();
        context.append("DATOS DEL SISTEMA ERP - COOLBOX:\n\n");

        // Si la consulta menciona productos
        if (query.toLowerCase().contains("producto") || query.toLowerCase().contains("inventario")) {
            List<Producto> productos = productoRepository.findByActivoTrue();
            context.append("PRODUCTOS ACTIVOS: ").append(productos.size()).append("\n");
            productos.stream().limit(10).forEach(p -> context.append("- ").append(p.getNombreProducto())
                    .append(" (").append(p.getMarcaProducto()).append(")\n"));
        }

        // Si la consulta menciona ventas
        if (query.toLowerCase().contains("venta") || query.toLowerCase().contains("vender")) {
            LocalDateTime hace7Dias = LocalDateTime.now().minusDays(7);
            List<Venta> ventasRecientes = ventaRepository.findByFechaVentaAfter(hace7Dias);
            context.append("\nVENTAS ÚLTIMOS 7 DÍAS: ").append(ventasRecientes.size()).append("\n");
        }

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
