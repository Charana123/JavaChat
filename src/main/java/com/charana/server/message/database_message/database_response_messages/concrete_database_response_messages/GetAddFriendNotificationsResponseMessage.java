package com.charana.server.message.database_message.database_response_messages.concrete_database_response_messages;

import com.charana.database_server.user.AddFriendNotification;
import com.charana.database_server.user.User;
import com.charana.server.message.database_message.Account;
import com.charana.server.message.database_message.FriendRequest;
import com.charana.server.message.database_message.database_response_messages.DatabaseResponseMessage;
import java.util.List;
import java.util.UUID;


public class GetAddFriendNotificationsResponseMessage extends DatabaseResponseMessage {
    public final List<FriendRequest> addFriendNotificationUsers;

    public GetAddFriendNotificationsResponseMessage(UUID clientID, boolean success, List<FriendRequest> addFriendNotificationUsers) {
        super(clientID, success);
        this.addFriendNotificationUsers = addFriendNotificationUsers;
    }
}
