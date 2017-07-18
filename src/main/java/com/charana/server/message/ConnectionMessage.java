package com.charana.server.message;

import javafx.scene.image.Image;

import java.util.UUID;

public class ConnectionMessage extends Message {
    public final String clientName; //Name of Client who sent the message (to display on every other clients screen)
    public final Image profilePic; //Profile picture of Client (to display on every other clients screen)

    public ConnectionMessage(UUID clientID, String clientName, Image profilePic){
        super(MessageType.CONNECTED, clientID);
        this.clientName = clientName;
        this.profilePic = profilePic;
    }
}
