package com.charana.server.message.database_message.database_response_messages.concrete_database_response_messages;

import com.charana.database_server.user.User;
import com.charana.server.message.database_message.Account;
import com.charana.server.message.database_message.database_response_messages.DatabaseResponseMessage;

import java.util.List;
import java.util.UUID;

/**
 * Created by Charana on 7/28/17.
 */
public class GetPossibleUsersResponseMessage extends DatabaseResponseMessage {
    public final List<Account> possibleUsers;

    public GetPossibleUsersResponseMessage(UUID clientID, boolean success, List<Account> possibleUsers) {
        super(clientID, success);
        this.possibleUsers = possibleUsers;
    }
}
