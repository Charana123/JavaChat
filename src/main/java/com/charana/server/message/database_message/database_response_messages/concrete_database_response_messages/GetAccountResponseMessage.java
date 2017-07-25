package com.charana.server.message.database_message.database_response_messages.concrete_database_response_messages;

import com.charana.database_server.user.User;
import com.charana.server.message.database_message.database_response_messages.DatabaseResponseMessage;
import java.util.UUID;


public class GetAccountResponseMessage extends DatabaseResponseMessage {
    User user;

    public GetAccountResponseMessage(UUID clientID, boolean success, User user) {
        super(clientID, success);
        this.user = user;
    }
}
