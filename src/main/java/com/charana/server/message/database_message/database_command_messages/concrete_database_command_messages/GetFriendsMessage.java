package com.charana.server.message.database_message.database_command_messages.concrete_database_command_messages;

import com.charana.server.message.database_message.database_command_messages.DatabaseCommandMessage;
import com.charana.server.message.database_message.database_command_messages.DatabaseCommandType;

import java.util.UUID;

/**
 * Created by Charana on 7/25/17.
 */
public class GetFriendsMessage extends DatabaseCommandMessage {
    public final String email;

    public GetFriendsMessage(UUID clientID, String email) {
        super(clientID, DatabaseCommandType.GET_FRIENDS);
        this.email = email;
    }
}
