package com.charana.server.message;

import java.util.UUID;

/**
 * Created by Charana on 7/17/17.
 */
public class PingMessage extends Message {

    public PingMessage(UUID clientID) {
        super(MessageType.PING, clientID);
    }
}
