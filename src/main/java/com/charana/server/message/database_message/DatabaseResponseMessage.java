package com.charana.server.message.database_message;

import java.util.UUID;

public class DatabaseResponseMessage extends DatabaseCommandMessage {
    public final boolean result;

    protected DatabaseResponseMessage(UUID clientID, DatabaseCommandType commandType, boolean result) {
        super(clientID, commandType);
        this.result = result;
    }

}
