package com.charana.server.message.database_message.concrete_database_message;

import com.charana.server.message.database_message.DatabaseCommandMessage;
import com.charana.server.message.database_message.DatabaseCommandType;

import java.util.UUID;

public class AccountExistsMessage extends DatabaseCommandMessage {
    public final String email;

    public AccountExistsMessage(UUID clientID, String email) {
        super(clientID, DatabaseCommandType.ACCOUNT_EXISTS);
        this.email = email;
    }
}
