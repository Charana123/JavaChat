package com.charana.server.message.database_message.database_command_messages.concrete_database_command_messages;

import com.charana.server.message.database_message.database_command_messages.DatabaseCommandMessage;
import com.charana.server.message.database_message.database_command_messages.DatabaseCommandType;
import java.util.UUID;

public class LoginMessage extends DatabaseCommandMessage {
    public final String email;
    public final String password;

    public LoginMessage(UUID clientID, String email, String password) {
        super(clientID, DatabaseCommandType.LOGIN);
        this.email = email;
        this.password = password;

    }
}
