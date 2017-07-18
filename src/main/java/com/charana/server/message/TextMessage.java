package com.charana.server.message;

import java.util.UUID;

public class TextMessage extends Message {
    public final String text_message;

    public TextMessage(MessageType type, UUID clientID, String text_message){
        super(MessageType.TEXT_MESSAGE, clientID);
        this.text_message = text_message;
    }
}
