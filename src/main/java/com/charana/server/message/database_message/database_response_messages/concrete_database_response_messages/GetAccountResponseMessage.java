package com.charana.server.message.database_message.database_response_messages.concrete_database_response_messages;

import com.charana.database_server.user.User;
import com.charana.server.message.database_message.Account;
import com.charana.server.message.database_message.database_response_messages.DatabaseResponseMessage;
import java.util.UUID;


public class GetAccountResponseMessage extends DatabaseResponseMessage {
    public final Account account;

    public GetAccountResponseMessage(UUID clientID, boolean success, Account account) {
        super(clientID, success);
        this.account = account;
    }
}
