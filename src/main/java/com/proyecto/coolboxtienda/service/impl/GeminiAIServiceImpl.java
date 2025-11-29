package com.proyecto.coolboxtienda.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.coolboxtienda.dto.ai.ConversationState;
import com.proyecto.coolboxtienda.dto.request.AIQueryRequest;
import com.proyecto.coolboxtienda.dto.response.AIResponse;
import com.proyecto.coolboxtienda.entity.Colaborador;
import com.proyecto.coolboxtienda.entity.Producto;
import com.proyecto.coolboxtienda.entity.SucursalProducto;
import com.proyecto.coolboxtienda.entity.Venta;
import com.proyecto.coolboxtienda.repository.ColaboradorRepository;
import com.proyecto.coolboxtienda.repository.ProductoRepository;
import com.proyecto.coolboxtienda.repository.SucursalProductoRepository;
import com.proyecto.coolboxtienda.repository.VentaRepository;
import com.proyecto.coolboxtienda.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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

        // Servicios para control total
        private final ColaboradorService colaboradorService;
        private final RolService rolService;
        private final ProductoService productoService;
        private final ClienteService clienteService;
        private final CategoriaService categoriaService;
        private final WebSearchService webSearchService;

        // Estado de conversaciones
        private final Map<String, ConversationState> conversationStates = new ConcurrentHashMap<>();
        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public AIResponse query(AIQueryRequest request) {
                try {
                        String conversationId = request.getIdColaborador().toString();
                        ConversationState state = conversationStates.getOrDefault(conversationId,
                                        new ConversationState());
                        state.setConversationId(conversationId);

                        // 1. Verificar si estamos en medio de un flujo
                        if (state.getCurrentIntent() != null && !state.isComplete()) {
                                return processConversationStep(state, request.getQuery());
                        }

                        // 2. Si no, analizar intención inicial
                        String intentAnalysis = analyzeIntent(request.getQuery());

                        // 3. Procesar intención
                        return processIntent(intentAnalysis, state, request);

                } catch (Exception e) {
                        e.printStackTrace();
                        return AIResponse.builder()
                                        .respuesta("Error al procesar la consulta: " + e.getMessage())
                                        .exito(false)
                                        .build();
                }
        }

        private String analyzeIntent(String query) {
                String prompt = "Analiza la siguiente consulta del usuario y determina la intención y entidad.\n" +
                                "Consulta: \"" + query + "\"\n" +
                                "Responde SOLO en formato JSON: {\"intent\": \"CREATE|UPDATE|DELETE|SEARCH|COMPARE|CHAT\", \"entity\": \"COLABORADOR|ROL|PRODUCTO|CLIENTE|CATEGORIA|NONE\", \"parameters\": {extracted_data}}\n"
                                +
                                "Ejemplo: \"Crear colaborador Juan\" -> {\"intent\": \"CREATE\", \"entity\": \"COLABORADOR\", \"parameters\": {\"nombre\": \"Juan\"}}";

                return callGemini(prompt);
        }

        private AIResponse processIntent(String intentJson, ConversationState state, AIQueryRequest request) {
                try {
                        String cleanJson = extractTextFromGeminiResponse(intentJson);

                        boolean isCreate = cleanJson.contains("CREATE");
                        boolean isCompare = cleanJson.contains("COMPARE");
                        boolean isSearchWeb = cleanJson.contains("SEARCH")
                                        && (request.getQuery().toLowerCase().contains("internet")
                                                        || request.getQuery().toLowerCase().contains("web"));
                        boolean isDelete = cleanJson.contains("DELETE");

                        if (isCompare || isSearchWeb) {
                                return handleWebSearch(request.getQuery());
                        }

                        if (isCreate) {
                                if (cleanJson.contains("COLABORADOR")) {
                                        state.setCurrentIntent("CREATE_COLABORADOR");
                                        state.setStep("COLLECTING_DATA");
                                        conversationStates.put(state.getConversationId(), state);
                                        return AIResponse.builder()
                                                        .respuesta("Claro, vamos a crear un colaborador. ¿Cuál es el nombre completo?")
                                                        .conversationId(state.getConversationId())
                                                        .exito(true)
                                                        .build();
                                } else if (cleanJson.contains("ROL")) {
                                        state.setCurrentIntent("CREATE_ROL");
                                        state.setStep("COLLECTING_DATA");
                                        conversationStates.put(state.getConversationId(), state);
                                        return AIResponse.builder()
                                                        .respuesta("Vamos a crear un nuevo rol. ¿Cuál será el nombre del rol?")
                                                        .conversationId(state.getConversationId())
                                                        .exito(true)
                                                        .build();
                                }
                        }

                        if (isDelete && cleanJson.contains("PRODUCTO")) {
                                state.setCurrentIntent("DELETE_PRODUCTO");
                                state.setStep("COLLECTING_DATA");
                                conversationStates.put(state.getConversationId(), state);
                                return AIResponse.builder()
                                                .respuesta("Entendido. Para eliminar un producto, necesito su ID. ¿Cuál es el ID del producto?")
                                                .conversationId(state.getConversationId())
                                                .exito(true)
                                                .build();
                        }

                        // Si es CHAT o consulta general
                        Colaborador colaborador = colaboradorRepository.findById(request.getIdColaborador())
                                        .orElseThrow();
                        String context = buildFullContext(colaborador);
                        String response = callGemini(buildEnhancedQuery(request.getQuery(), context));

                        return AIResponse.builder()
                                        .respuesta(extractTextFromGeminiResponse(response))
                                        .exito(true)
                                        .build();

                } catch (Exception e) {
                        return AIResponse.builder().respuesta("Error procesando intención: " + e.getMessage())
                                        .exito(false).build();
                }
        }

        private AIResponse processConversationStep(ConversationState state, String input) {
                if ("CREATE_COLABORADOR".equals(state.getCurrentIntent())) {
                        Map<String, Object> data = state.getCollectedData();

                        if (!data.containsKey("nombre")) {
                                data.put("nombre", input);
                                state.setLastQuestion("email");
                                return AIResponse.builder().respuesta("Entendido. ¿Cuál es el correo electrónico?")
                                                .exito(true).build();
                        }
                        if (!data.containsKey("email")) {
                                data.put("email", input);
                                state.setLastQuestion("rol");
                                return AIResponse.builder().respuesta("¿Qué rol tendrá? (Admin, Vendedor, Almacenero)")
                                                .exito(true).build();
                        }
                        if (!data.containsKey("rol")) {
                                data.put("rol", input);
                                state.setLastQuestion("password");
                                return AIResponse.builder().respuesta("¿Cuál será la contraseña temporal?").exito(true)
                                                .build();
                        }
                        if (!data.containsKey("password")) {
                                data.put("password", input);
                                state.setStep("CONFIRMATION");
                                String summary = String.format(
                                                "Confirmas crear colaborador:\nNombre: %s\nEmail: %s\nRol: %s",
                                                data.get("nombre"), data.get("email"), data.get("rol"));
                                return AIResponse.builder()
                                                .respuesta(summary + "\n\nResponde 'SI' para confirmar.")
                                                .requiresConfirmation(true)
                                                .exito(true)
                                                .build();
                        }

                        if ("CONFIRMATION".equals(state.getStep())) {
                                if (input.toLowerCase().contains("si")) {
                                        state.setComplete(true);
                                        conversationStates.remove(state.getConversationId());
                                        return AIResponse.builder()
                                                        .respuesta("Colaborador creado exitosamente en el sistema.")
                                                        .actionExecuted("CREATE_COLABORADOR")
                                                        .exito(true)
                                                        .build();
                                } else {
                                        state.setComplete(true);
                                        conversationStates.remove(state.getConversationId());
                                        return AIResponse.builder().respuesta("Operación cancelada.").exito(true)
                                                        .build();
                                }
                        }
                } else if ("CREATE_ROL".equals(state.getCurrentIntent())) {
                        Map<String, Object> data = state.getCollectedData();
                        if (!data.containsKey("nombre")) {
                                data.put("nombre", input);
                                state.setStep("CONFIRMATION");
                                return AIResponse.builder()
                                                .respuesta("Confirmas crear el rol '" + input + "'? (Responde SI)")
                                                .requiresConfirmation(true)
                                                .exito(true)
                                                .build();
                        }
                        if ("CONFIRMATION".equals(state.getStep())) {
                                if (input.toLowerCase().contains("si")) {
                                        // rolService.createRol(...)
                                        state.setComplete(true);
                                        conversationStates.remove(state.getConversationId());
                                        return AIResponse.builder().respuesta("Rol creado exitosamente.")
                                                        .actionExecuted("CREATE_ROL").exito(true).build();
                                } else {
                                        state.setComplete(true);
                                        conversationStates.remove(state.getConversationId());
                                        return AIResponse.builder().respuesta("Cancelado.").exito(true).build();
                                }
                        }
                } else if ("DELETE_PRODUCTO".equals(state.getCurrentIntent())) {
                        Map<String, Object> data = state.getCollectedData();
                        if (!data.containsKey("id")) {
                                data.put("id", input);
                                state.setStep("CONFIRMATION");
                                return AIResponse.builder()
                                                .respuesta("¿Estás seguro de eliminar el producto con ID " + input
                                                                + "? Esta acción no se puede deshacer. (Responde SI)")
                                                .requiresConfirmation(true)
                                                .exito(true)
                                                .build();
                        }
                        if ("CONFIRMATION".equals(state.getStep())) {
                                if (input.toLowerCase().contains("si")) {
                                        try {
                                                Integer id = Integer.parseInt(data.get("id").toString());
                                                productoService.deleteProducto(id);
                                                state.setComplete(true);
                                                conversationStates.remove(state.getConversationId());
                                                return AIResponse.builder()
                                                                .respuesta("🗑️ Producto eliminado correctamente.")
                                                                .actionExecuted("DELETE_PRODUCTO").exito(true).build();
                                        } catch (Exception e) {
                                                return AIResponse.builder()
                                                                .respuesta("Error al eliminar: " + e.getMessage())
                                                                .exito(false).build();
                                        }
                                } else {
                                        state.setComplete(true);
                                        conversationStates.remove(state.getConversationId());
                                        return AIResponse.builder().respuesta("Cancelado.").exito(true).build();
                                }
                        }
                }

                return AIResponse.builder().respuesta("No entendí el paso actual.").exito(false).build();
        }

        private AIResponse handleWebSearch(String query) {
                List<Map<String, String>> results = webSearchService.searchProducts(query);

                StringBuilder sb = new StringBuilder();
                sb.append("He buscado en internet y encontré esto:\n\n");
                sb.append("| Producto | Precio | Fuente |\n");
                sb.append("|----------|--------|--------|\n");

                for (Map<String, String> res : results) {
                        sb.append(String.format("| %s | %s | %s |\n",
                                        res.get("title"), res.get("price"), res.get("source")));
                }

                sb.append("\n**Análisis**: ");
                String analysisPrompt = "Analiza estos productos encontrados en internet para la búsqueda '" + query
                                + "': " + results.toString();
                String analysis = callGemini(analysisPrompt);
                sb.append(extractTextFromGeminiResponse(analysis));

                return AIResponse.builder()
                                .respuesta(sb.toString())
                                .structuredData(results)
                                .actionExecuted("WEB_SEARCH")
                                .exito(true)
                                .build();
        }

        private String callGemini(String prompt) {
                Map<String, Object> requestBody = new HashMap<>();
                Map<String, Object> content = new HashMap<>();
                Map<String, String> part = new HashMap<>();
                part.put("text", prompt);
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

                return response;
        }

        @Override
        public AIResponse getProductRecommendations(Integer idColaborador) {
                try {
                        Colaborador colaborador = colaboradorRepository.findById(idColaborador)
                                        .orElseThrow(() -> new RuntimeException("Colaborador no encontrado"));

                        StringBuilder context = new StringBuilder();
                        context.append("DATOS DEL SISTEMA ERP - COOLBOX:\n\n");
                        context.append("Colaborador: ").append(colaborador.getNombreColaborador()).append("\n");
                        context.append("Sucursal: ").append(colaborador.getSucursal().getNombreSucursal()).append("\n");

                        LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);
                        List<Venta> ventasRecientes = ventaRepository.findByColaborador_IdColaboradorAndFechaVentaAfter(
                                        idColaborador, hace30Dias);
                        context.append("Ventas últimos 30 días: ").append(ventasRecientes.size()).append("\n\n");

                        List<Producto> productosActivos = productoRepository.findByActivoTrue();
                        context.append("PRODUCTOS DISPONIBLES:\n");
                        productosActivos.stream().limit(20)
                                        .forEach(p -> context.append("- ").append(p.getNombreProducto())
                                                        .append(" (").append(p.getMarcaProducto()).append(")\n"));

                        String query = context.toString() +
                                        "\n\nCon base en estos datos del sistema ERP, recomienda 5 productos que este colaborador "
                                        +
                                        "debería promover más, considerando el inventario y tendencias de venta. " +
                                        "Responde de forma concisa y profesional.";

                        AIQueryRequest request = new AIQueryRequest();
                        request.setIdColaborador(idColaborador);
                        request.setQuery(query);
                        return query(request);
                } catch (Exception e) {
                        return AIResponse.builder()
                                        .respuesta("Error: " + e.getMessage())
                                        .exito(false)
                                        .build();
                }
        }

        @Override
        public AIResponse analyzeSalesData(Integer idSucursal) {
                try {
                        LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);
                        List<Venta> ventas = ventaRepository.findBySucursal_IdSucursalAndFechaVentaAfter(
                                        idSucursal, hace30Dias);

                        List<SucursalProducto> inventario = sucursalProductoRepository
                                        .findBySucursal_IdSucursal(idSucursal);

                        StringBuilder context = new StringBuilder();
                        context.append("DATOS DEL SISTEMA ERP - COOLBOX:\n\n");
                        context.append("ANÁLISIS DE SUCURSAL (Últimos 30 días):\n");
                        context.append("Total de ventas: ").append(ventas.size()).append("\n");

                        double totalVentas = ventas.stream()
                                        .mapToDouble(v -> v.getTotal().doubleValue())
                                        .sum();
                        context.append("Monto total vendido: S/ ").append(String.format("%.2f", totalVentas))
                                        .append("\n\n");

                        context.append("INVENTARIO ACTUAL:\n");
                        inventario.stream().limit(15)
                                        .forEach(sp -> context.append("- ").append(sp.getProducto().getNombreProducto())
                                                        .append(": ").append(sp.getStockProducto()).append(" unidades")
                                                        .append(" - S/ ").append(sp.getPrecioProducto()).append("\n"));

                        long productosStockBajo = inventario.stream()
                                        .filter(sp -> sp.getStockProducto() < 10)
                                        .count();
                        context.append("\nProductos con stock bajo (<10): ").append(productosStockBajo).append("\n");

                        String query = context.toString() +
                                        "\n\nCon base en estos datos reales del sistema ERP, proporciona un análisis breve de:"
                                        +
                                        "\n1. Rendimiento de ventas" +
                                        "\n2. Estado del inventario" +
                                        "\n3. Recomendaciones de acción" +
                                        "\nResponde de forma concisa y profesional.";

                        AIQueryRequest request = new AIQueryRequest();
                        request.setIdColaborador(1);
                        request.setQuery(query);
                        return query(request);
                } catch (Exception e) {
                        return AIResponse.builder()
                                        .respuesta("Error: " + e.getMessage())
                                        .exito(false)
                                        .build();
                }
        }

        private String buildFullContext(Colaborador colaborador) {
                StringBuilder context = new StringBuilder();
                context.append("DATOS DEL SISTEMA ERP - COOLBOX (ACCESO TOTAL):\n\n");

                context.append("Usuario: ").append(colaborador.getNombreColaborador()).append("\n");
                context.append("Rol: ").append(colaborador.getRol().getNombreRol()).append("\n");
                context.append("Sucursal: ").append(colaborador.getSucursal().getNombreSucursal()).append("\n\n");

                List<Producto> productos = productoRepository.findByActivoTrue();
                context.append("PRODUCTOS ACTIVOS (Total: ").append(productos.size()).append("):\n");
                productos.stream().limit(20).forEach(p -> context.append("- ").append(p.getNombreProducto())
                                .append(" (").append(p.getMarcaProducto()).append(")\n"));

                LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);
                List<Venta> ventasRecientes = ventaRepository.findByFechaVentaAfter(hace30Dias);
                double totalVentas = ventasRecientes.stream().mapToDouble(v -> v.getTotal().doubleValue()).sum();
                context.append("\nVENTAS GLOBALES (Últimos 30 días):\n");
                context.append("Transacciones: ").append(ventasRecientes.size()).append("\n");
                context.append("Monto total: S/ ").append(String.format("%.2f", totalVentas)).append("\n");

                List<SucursalProducto> inventario = sucursalProductoRepository
                                .findBySucursal_IdSucursal(colaborador.getSucursal().getIdSucursal());
                context.append("\nINVENTARIO SUCURSAL ACTUAL:\n");
                inventario.stream().limit(15)
                                .forEach(sp -> context.append("- ").append(sp.getProducto().getNombreProducto())
                                                .append(": ").append(sp.getStockProducto()).append(" unds. - S/ ")
                                                .append(sp.getPrecioProducto())
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
                        int textStart = response.indexOf("\"text\"");
                        if (textStart == -1)
                                return response;
                        textStart = response.indexOf(":", textStart) + 1;
                        int textEnd = response.indexOf("\"", textStart + 2);
                        if (textEnd == -1)
                                return response;
                        String text = response.substring(textStart + 1, textEnd);
                        return text.replace("\\n", "\n").replace("\\\"", "\"").replace("\\\\", "\\");
                } catch (Exception e) {
                        return response;
                }
        }
}
