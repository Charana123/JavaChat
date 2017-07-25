package com.charana.server.message.database_message.database_command_messages.concrete_database_command_messages;

import com.charana.database_server.user.User;
import com.charana.server.message.database_message.database_command_messages.DatabaseCommandMessage;
import com.charana.server.message.database_message.database_command_messages.DatabaseCommandType;

import java.util.UUID;

/**
 * Created by Charana on 7/16/17.
 */
public class CreateAccountMessage extends DatabaseCommandMessage{
    public final User user;

    public CreateAccountMessage(UUID clientID, User user) {
        super(clientID, DatabaseCommandType.CREATE_ACCOUNT);
        this.user = user;
    }
}
