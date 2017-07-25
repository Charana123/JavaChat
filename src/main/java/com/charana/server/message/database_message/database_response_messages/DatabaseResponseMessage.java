package com.charana.server.message.database_message.database_response_messages;

import com.charana.server.message.Message;
import com.charana.server.message.MessageType;

import java.util.UUID;

public class DatabaseResponseMessage extends Message {
    public final boolean success;

    public DatabaseResponseMessage(UUID clientID, boolean success) {
        super(MessageType.DATABASE_RESPONSE, clientID);
        this.success = success;
    }
}
