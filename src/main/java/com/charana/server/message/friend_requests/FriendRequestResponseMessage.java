package com.charana.server.message.friend_requests;

import com.charana.server.message.Message;
import com.charana.server.message.MessageType;

import java.util.UUID;

public class FriendRequestResponseMessage extends Message {
    public final String sourceUserEmail, targetUserEmail;
    public final FriendRequestResponseType responseType;

    public FriendRequestResponseMessage(UUID clientID, String sourceUserEmail, String targetUserEmail, FriendRequestResponseType responseType) {
        super(MessageType.FRIEND_REQUEST_RESPONSE, clientID);
        this.sourceUserEmail = sourceUserEmail;
        this.targetUserEmail = targetUserEmail;
        this.responseType = responseType;
    }
}
