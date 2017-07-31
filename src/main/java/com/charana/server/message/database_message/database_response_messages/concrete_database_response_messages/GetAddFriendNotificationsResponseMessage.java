package com.charana.server.message.database_message.database_response_messages.concrete_database_response_messages;

import com.charana.database_server.user.AddFriendNotification;
import com.charana.server.message.database_message.database_response_messages.DatabaseResponseMessage;
import java.util.List;
import java.util.UUID;


public class GetAddFriendNotificationsResponseMessage extends DatabaseResponseMessage {
    public final List<AddFriendNotification> addFriendNotification;

    public GetAddFriendNotificationsResponseMessage(UUID clientID, boolean success, List<AddFriendNotification> addFriendNotification) {
        super(clientID, success);
        this.addFriendNotification = addFriendNotification;
    }
}
