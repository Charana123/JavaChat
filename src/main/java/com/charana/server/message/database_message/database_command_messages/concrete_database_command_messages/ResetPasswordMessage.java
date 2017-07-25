package com.charana.server.message.database_message.database_command_messages.concrete_database_command_messages;

import com.charana.server.message.database_message.database_command_messages.DatabaseCommandMessage;
import com.charana.server.message.database_message.database_command_messages.DatabaseCommandType;
import java.util.UUID;

public class ResetPasswordMessage extends DatabaseCommandMessage {
    public final String email;
    public final String newPassword;

    public ResetPasswordMessage(UUID clientID, String email, String newPassword) {
        super(clientID, DatabaseCommandType.RESET_PASSWORD);
        this.email = email;
        this.newPassword = newPassword;
    }
}
