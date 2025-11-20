package com.proyecto.coolboxtienda.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequest {

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombreProducto;

    @NotBlank(message = "La marca es obligatoria")
    @Size(max = 50, message = "La marca no puede exceder 50 caracteres")
    private String marcaProducto;

    @NotBlank(message = "El modelo es obligatorio")
    @Size(max = 100, message = "El modelo no puede exceder 100 caracteres")
    private String modeloProducto;

    @NotNull(message = "La categoría es obligatoria")
    private Integer idCategoria;

    private String dimensionesProducto;

    private String especificacionesProducto;

    private BigDecimal pesoProducto;

    // La imagen se maneja por separado como MultipartFile en el controller
}
