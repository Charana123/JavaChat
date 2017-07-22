package com.charana.server.message.database_message;

import com.charana.server.message.Message;
import com.charana.server.message.MessageType;

import java.util.UUID;

public class DatabaseResponseMessage extends Message {
    public final boolean result;

    public DatabaseResponseMessage(UUID clientID, boolean result) {
        super(MessageType.DATABASE_RESPONSE, clientID);
        this.result = result;
    }
}
