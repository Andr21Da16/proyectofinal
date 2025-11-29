package com.proyecto.coolboxtienda.dto.ai;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Data
public class ConversationState {
    private String conversationId;
    private String currentIntent; // CREATE_COLABORADOR, CREATE_ROL, etc.
    private String step; // COLLECTING_DATA, CONFIRMATION, EXECUTING
    private Map<String, Object> collectedData = new HashMap<>();
    private String lastQuestion;
    private boolean isComplete;

    public void addData(String key, Object value) {
        collectedData.put(key, value);
    }
}
