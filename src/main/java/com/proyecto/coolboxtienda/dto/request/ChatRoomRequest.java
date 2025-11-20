package com.proyecto.coolboxtienda.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomRequest {

    @NotBlank(message = "El nombre de la sala es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombreRoom;

    @Size(max = 20, message = "El tipo no puede exceder 20 caracteres")
    private String tipoRoom; // INDIVIDUAL, GRUPO (default: GRUPO)

    @NotEmpty(message = "Debe incluir al menos un participante")
    private List<Integer> participantes;
}
