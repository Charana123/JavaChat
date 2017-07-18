package com.charana.server.message.database_message.concrete_database_message;

import com.charana.server.message.database_message.DatabaseCommandMessage;
import com.charana.server.message.database_message.DatabaseCommandType;
import java.util.UUID;

public class LoginMessage extends DatabaseCommandMessage {
    String email;
    String password;

    public LoginMessage(UUID clientID, String email, String password) {
        super(clientID, DatabaseCommandType.LOGIN);
        this.email = email;
        this.password = password;

    }
}
