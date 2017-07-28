package com.charana.server.message.database_message.database_response_messages.concrete_database_response_messages;

import com.charana.database_server.user.User;
import com.charana.server.message.database_message.database_response_messages.DatabaseResponseMessage;
import java.util.List;
import java.util.UUID;

public class GetFriendsResponseMessage extends DatabaseResponseMessage{
    public final List<User> friends;

    public GetFriendsResponseMessage(UUID clientID, boolean success, List<User> friends) {
        super(clientID, success);
        this.friends = friends;
    }
}
