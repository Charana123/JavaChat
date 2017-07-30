package com.charana.server.message.database_message.database_response_messages.concrete_database_response_messages;

import com.charana.database_server.user.AddFriendNotification;
import com.charana.server.message.database_message.database_response_messages.DatabaseResponseMessage;
import java.util.List;
import java.util.UUID;


public class GetAddFriendNotificationsResponseMessage extends DatabaseResponseMessage {
    public final List<AddFriendNotification> addFriendNotifications;

    public GetAddFriendNotificationsResponseMessage(UUID clientID, boolean success, List<AddFriendNotification> addFriendNotifications) {
        super(clientID, success);
        this.addFriendNotifications = addFriendNotifications;
    }
}
