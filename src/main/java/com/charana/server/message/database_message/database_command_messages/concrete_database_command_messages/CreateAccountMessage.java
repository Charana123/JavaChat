package com.charana.server.message.database_message.database_command_messages.concrete_database_command_messages;

import com.charana.database_server.user.User;
import com.charana.server.message.database_message.Account;
import com.charana.server.message.database_message.database_command_messages.DatabaseCommandMessage;
import com.charana.server.message.database_message.database_command_messages.DatabaseCommandType;

import java.util.UUID;


public class CreateAccountMessage extends DatabaseCommandMessage{
    public final Account account;

    public CreateAccountMessage(UUID clientID, Account account) {
        super(clientID, DatabaseCommandType.CREATE_ACCOUNT);
        this.account = account;
    }
}
