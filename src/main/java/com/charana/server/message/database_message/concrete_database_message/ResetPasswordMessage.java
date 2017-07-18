package com.charana.server.message.database_message.concrete_database_message;

import com.charana.server.message.database_message.DatabaseCommandMessage;
import com.charana.server.message.database_message.DatabaseCommandType;
import java.util.UUID;

public class ResetPasswordMessage extends DatabaseCommandMessage {
    String email;
    String newPassword;

    public ResetPasswordMessage(UUID clientID, String email, String newPassword) {
        super(clientID, DatabaseCommandType.RESET_PASSWORD);
        this.email = email;
        this.newPassword = newPassword;
    }
}
