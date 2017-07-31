package com.charana.server.message.friend_requests;

import com.charana.server.message.Message;
import com.charana.server.message.MessageType;

import java.util.UUID;

/**
 * Created by Charana on 7/30/17.
 */
public class FriendRequestResponseMessage extends Message {
    public final FriendRequestResponseType responseType;

    public FriendRequestResponseMessage(UUID clientID, FriendRequestResponseType responseType) {
        super(MessageType.FRIEND_REQUEST_RESPONSE, clientID);
        this.responseType = responseType;
    }
}
