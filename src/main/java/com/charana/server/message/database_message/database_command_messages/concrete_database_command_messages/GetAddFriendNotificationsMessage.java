package com.charana.server.message.database_message.database_command_messages.concrete_database_command_messages;

import com.charana.server.message.database_message.database_command_messages.DatabaseCommandMessage;
import com.charana.server.message.database_message.database_command_messages.DatabaseCommandType;
import java.util.UUID;

public class GetAddFriendNotificationsMessage extends DatabaseCommandMessage {
    public final String email;

    public GetAddFriendNotificationsMessage(UUID clientID, String email) {
        super(clientID, DatabaseCommandType.GET_ADD_FRIEND_NOTIFICATIONS);
        this.email = email;
    }
}
