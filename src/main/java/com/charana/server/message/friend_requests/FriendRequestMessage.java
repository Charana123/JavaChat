package com.charana.server.message.friend_requests;

import com.charana.server.message.Message;
import com.charana.server.message.MessageType;

import java.util.UUID;

/**
 * Created by Charana on 7/30/17.
 */
public class FriendRequestMessage extends Message {
    public final String sourceUserEmail, targetUserEmail;

    public FriendRequestMessage(UUID clientID, String sourceUserEmail, String targetUserEmail) {
        super(MessageType.FRIEND_REQUEST, clientID);
        this.sourceUserEmail = sourceUserEmail;
        this.targetUserEmail = targetUserEmail;
    }
}
