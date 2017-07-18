package com.charana.server;

import com.charana.server.message.*;

import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class Server {
    public final int serverPort = 8192;
    private final Logger logger = LoggerFactory.getLogger(Server.class);
    private boolean serverisRunning;
    //Such that only one client manager thread can be broadcasting a message to all other clients
    private List<ServerClient> clients = Collections.synchronizedList(new ArrayList<>());

    public Server() throws IOException {
        ServerSocket connectionListener = new ServerSocket(serverPort);

        //Start thread (connectionManager) that create threads that manages individual connections with clients
        Thread connectionManager = new Thread("CONNECTION MANAGER") {
            @Override
            public void run() {
                serverisRunning = true;
                while (serverisRunning) {
                    try {
                        Socket connection = connectionListener.accept();
                        new Thread(new clientConnectionManager(connection)).start();
                        logger.info("Connection made");
                    } catch (IOException e) { //If connecting so a single client connection fails, WARNING + continue waiting for other connections
                        logger.warn("Client connection failed");
                    }
                }
            }
        };
        connectionManager.start();
    }

    //Handles communication with a client, receiving, sending, of messages
    private class clientConnectionManager implements Runnable {
        private final Logger logger = LoggerFactory.getLogger(clientConnectionManager.class);
        private final Socket connection; //Final values can only be set during initialization and in the constructor
        private ObjectOutputStream thisClientOutStream;
        private ServerClient thisClient;
        private boolean hasConnected = false; //Whether client has send a connection message to register with the server
        private String clientName;

        clientConnectionManager(Socket connection){
            this.connection = connection;
            clientName = connection.getInetAddress().getHostAddress() + ":" + connection.getPort();
        }


        @Override
        public void run() {
            try{
                ObjectInputStream fromClientStream = new ObjectInputStream(connection.getInputStream());
                thisClientOutStream = new ObjectOutputStream(connection.getOutputStream());
                thisClientOutStream.flush();

                while(!connection.isClosed()){
                    try {
                        connection.setSoTimeout(5000);
                        Message messagefromClient = (Message) fromClientStream.readObject();
                        processMessage(messagefromClient); }
                    //If message could not be read from client, ERROR + terminate current client manager thread
                    //A TCP connection must guarantee reliability of communication
                    catch(EOFException e){
                        logger.error("Message could not be read from client {} (peer socket closed)", clientName, e);
                        closeThisConnection();
                        return;
                    }
                    catch(SocketTimeoutException e){
                        logger.error("Lost TCP connection with client {} (no message in 5 seconds)", clientName, e);
                        closeThisConnection();
                        return;
                    }
                    catch (IOException | ClassNotFoundException e) {
                        logger.error("Message could not be read from client {} (unsure cause) ", clientName, e);
                        closeThisConnection();
                        return;
                    }
                }
            }
            /*If getting input/output streams to a client fails, ERROR + terminate current client manager thread
            A TCP connection must guarantee reliability of communication */
            catch(IOException e) {
                logger.error("Getting Input/Output stream from/to client {} failed", clientName, e);
                closeThisConnection();
            }
        }

        private void processMessage(Message message){
            switch(message.type){
                case CONNECTED:
                    addClient((ConnectionMessage) message);
                    break;
                case TEXT_MESSAGE:
                    sendToAll((TextMessage) message);
                    break;
                case PING: //Responds to PING messages regardless of whether the client has sent a Connection Message
                    send(new PingMessage(null));
                case DISCONNECTED:
                    break;
                case VOICE_MESSAGE:
                    break;
                case SERVER:
                    break;
            }
        }

        private void sendToAll(TextMessage text_message){
            logger.info("Client: " + thisClient.name + " sending text message: " + text_message);
            //Such that no modifications occur to the client list (add or remove) during traversal/iteration in the below code
            synchronized (clients){
                clients.stream().forEach(client -> {
                    try { //The client manager that is broadcasting a message to every client synchronizes with the client manager
                        //thread that owns the client
                        synchronized (client.toClientStream){
                            client.toClientStream.writeObject(text_message);
                        }
                    }
                    catch (IOException e) { //If message could not be transferred to client, Log ERROR of the lost message
                        logger.error("Message {} could not be sent to client {}", new GsonBuilder().create().toJson(text_message), client.name);
                    }
                });
            }
        }

        private void send(Message message){
            try {
                synchronized (thisClientOutStream){
                    thisClientOutStream.writeObject(message);
                }
            }
            catch (IOException e) { //If message would not to transferred to managed client, Log ERROR of the lost message
                logger.error("Message {} could not be sent to client {} (socket closed)", new GsonBuilder().create().toJson(message), thisClient.name, e);
            }
        }

        private void addClient(ConnectionMessage connection_message){
            logger.info("Connection made with client {} ", connection_message.clientName);
            ServerClient client = new ServerClient(connection_message.clientName, thisClientOutStream, connection);
            this.thisClient = client;
            clientName = client.name;
            hasConnected = true;
            clients.add(client);
        }

        /**
         * Closing the connection with a client managed by the current thread
         */
        private void closeThisConnection(){
            //Remove the client from the server, if the client has previously connected
            if(!Objects.isNull(thisClient)) { clients.remove(thisClient); }
            logger.info("Closing connection with client {}",  connection.getInetAddress().getAddress(), connection.getPort());
            try { connection.close(); }
            catch (IOException e) { logger.error("Could not close connection/socket", e); }
        }
    }


    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger("Server (main)");
        try { Server server = new Server(); }
        //If server could not be creates, ERROR + Terminate application
        catch (IOException e) { logger.error("Server creation failed"); return; }
    }
}

