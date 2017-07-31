package com.charana.server.message.database_message.database_response_messages.concrete_database_response_messages;

import com.charana.database_server.user.AddFriendNotificationDB;
import com.charana.server.message.database_message.database_response_messages.DatabaseResponseMessage;
import java.util.List;
import java.util.UUID;


public class GetAddFriendNotificationsResponseMessage extends DatabaseResponseMessage {
    public final List<AddFriendNotificationDB> addFriendNotificationDB;

    public GetAddFriendNotificationsResponseMessage(UUID clientID, boolean success, List<AddFriendNotificationDB> addFriendNotificationDB) {
        super(clientID, success);
        this.addFriendNotificationDB = addFriendNotificationDB;
    }
}
