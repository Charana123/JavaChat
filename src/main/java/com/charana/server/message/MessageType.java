package com.charana.server.message;

//MessageType.PING isn't required as TCP maintains a connection between Client & Server.
public enum MessageType {
    TEXT_MESSAGE //Text Message from Client to all other Clients
    , VOICE_MESSAGE //Audio Message from Client to all other Clients
    , CONNECTED //Client Connection to the Chat Server
    , DISCONNECTED //Client Disconnected from Chat Server
    , SERVER //Test Message from Server to all Clients
    , DATABASE_COMMAND
    , DATABASE_RESPONSE
    , PING //To implement the HeartBeat Protocol
    , FRIEND_REQUEST
    , FRIEND_REQUEST_RESPONSE
}