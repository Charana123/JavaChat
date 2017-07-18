package com.charana.server.message;

import java.util.UUID;

public class VoiceMessage extends Message {
    public final byte[] voiceMSg;

    public VoiceMessage(MessageType type, UUID clientID, byte[] voiceMSg){
        super(MessageType.VOICE_MESSAGE, clientID);
        this.voiceMSg = voiceMSg;
    }
}
