package com.charana.server.message;

import java.util.UUID;

public class TextMessage extends Message {
    public final String text_message;
    public final String sourceUserEmail;

    public TextMessage(UUID clientID, String text_message, String sourceUserEmail){
        super(MessageType.TEXT_MESSAGE, clientID);
        this.text_message = text_message;
        this.sourceUserEmail = sourceUserEmail;
    }
}
