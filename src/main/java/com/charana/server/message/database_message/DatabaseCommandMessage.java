package com.charana.server.message.database_message;

import com.charana.server.message.Message;
import com.charana.server.message.MessageType;
import java.util.UUID;

public class DatabaseCommandMessage extends Message {
    public final DatabaseCommandType commandType;

    protected DatabaseCommandMessage(UUID clientID, DatabaseCommandType commandType) {
        super(MessageType.DATABASE_COMMAND, clientID);
        this.commandType = commandType;
    }


}
