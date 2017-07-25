package com.charana.server.message.database_message.database_command_messages.concrete_database_command_messages;

import com.charana.server.message.database_message.database_command_messages.DatabaseCommandMessage;
import com.charana.server.message.database_message.database_command_messages.DatabaseCommandType;

import java.util.UUID;

/**
 * Created by Charana on 7/22/17.
 */
public class GetAccountMessage extends DatabaseCommandMessage{
    public final String email;

    public GetAccountMessage(UUID clientID, String email) {
        super(clientID, DatabaseCommandType.GET_ACCOUNT);
        this.email = email;
    }
}
