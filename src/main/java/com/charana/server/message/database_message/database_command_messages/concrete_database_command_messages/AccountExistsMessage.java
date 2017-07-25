package com.charana.server.message.database_message.database_command_messages.concrete_database_command_messages;

import com.charana.server.message.database_message.database_command_messages.DatabaseCommandMessage;
import com.charana.server.message.database_message.database_command_messages.DatabaseCommandType;

import java.util.UUID;

public class AccountExistsMessage extends DatabaseCommandMessage {
    public final String email;

    public AccountExistsMessage(UUID clientID, String email) {
        super(clientID, DatabaseCommandType.ACCOUNT_EXISTS);
        this.email = email;
    }
}
