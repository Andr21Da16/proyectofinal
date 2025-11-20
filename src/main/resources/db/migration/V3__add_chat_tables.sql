-- V3: Add chat system tables

CREATE TABLE chat_rooms (
    id_room SERIAL NOT NULL,
    nombre_room VARCHAR(100) NOT NULL,
    tipo_room VARCHAR(20) NOT NULL, -- 'INDIVIDUAL', 'GRUPO'
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    CONSTRAINT id_room_pk PRIMARY KEY (id_room)
);

CREATE TABLE chat_participants (
    id_room INTEGER NOT NULL,
    id_colaborador INTEGER NOT NULL,
    fecha_union TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    CONSTRAINT chat_participants_pk PRIMARY KEY (id_room, id_colaborador),
    CONSTRAINT id_room_cp_fk FOREIGN KEY (id_room)
        REFERENCES chat_rooms (id_room)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT id_colaborador_cp_fk FOREIGN KEY (id_colaborador)
        REFERENCES colaborador (id_colaborador)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE chat_messages (
    id_message SERIAL NOT NULL,
    id_room INTEGER NOT NULL,
    id_colaborador INTEGER NOT NULL,
    mensaje TEXT NOT NULL,
    fecha_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    leido BOOLEAN DEFAULT FALSE,
    tipo_mensaje VARCHAR(20) DEFAULT 'TEXT', -- 'TEXT', 'FILE', 'IMAGE'
    CONSTRAINT id_message_pk PRIMARY KEY (id_message),
    CONSTRAINT id_room_cm_fk FOREIGN KEY (id_room)
        REFERENCES chat_rooms (id_room)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT id_colaborador_cm_fk FOREIGN KEY (id_colaborador)
        REFERENCES colaborador (id_colaborador)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE chat_online_status (
    id_colaborador INTEGER NOT NULL,
    online BOOLEAN DEFAULT FALSE,
    ultima_actividad TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT id_colaborador_online_pk PRIMARY KEY (id_colaborador),
    CONSTRAINT id_colaborador_online_fk FOREIGN KEY (id_colaborador)
        REFERENCES colaborador (id_colaborador)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX idx_chat_messages_room ON chat_messages(id_room);
CREATE INDEX idx_chat_messages_fecha ON chat_messages(fecha_envio);
CREATE INDEX idx_chat_messages_leido ON chat_messages(leido);
CREATE INDEX idx_chat_participants_colaborador ON chat_participants(id_colaborador);
