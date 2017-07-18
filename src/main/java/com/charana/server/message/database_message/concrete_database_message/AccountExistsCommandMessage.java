package com.charana.server.message.database_message.concrete_database_message;

import com.charana.server.message.database_message.DatabaseCommandMessage;
import com.charana.server.message.database_message.DatabaseCommandType;

import java.util.UUID;

public class AccountExistsCommandMessage extends DatabaseCommandMessage {
    String email;

    public AccountExistsCommandMessage(UUID clientID, String email) {
        super(clientID, DatabaseCommandType.ACCOUNT_EXISTS);
        this.email = email;
    }
}
