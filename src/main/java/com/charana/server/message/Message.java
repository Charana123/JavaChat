package com.charana.server.message;

import java.io.Serializable;
import java.util.UUID;

public abstract class Message implements Serializable {
    public final MessageType type; //Type of Message
    public final UUID clientID; //Unique ID of Client (to Distinguish which Client sent the message)

    protected Message(MessageType type, UUID clientID){
        this.type = type;
        this.clientID = clientID;
    }
}
