package com.charana.server;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerClient {
    final String name;
    final ObjectOutputStream toClientStream;
    final Socket connection;

    ServerClient(String name, ObjectOutputStream toClientStream, Socket connection){
        this.name = name;
        this.toClientStream = toClientStream;
        this.connection = connection;
    }
}
