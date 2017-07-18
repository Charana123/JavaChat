package com.charana.server.message.database_message.concrete_database_message;

import com.charana.login_window.utilities.database.user.User;
import com.charana.server.message.database_message.DatabaseCommandMessage;
import com.charana.server.message.database_message.DatabaseCommandType;

import java.util.UUID;

/**
 * Created by Charana on 7/16/17.
 */
public class CreateAccountMessage extends DatabaseCommandMessage{
    User user;

    public CreateAccountMessage(UUID clientID, User user) {
        super(clientID, DatabaseCommandType.CREATE_ACCOUNT);
        this.user = user;
    }
}
