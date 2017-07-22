package com.charana.login_window.utilities.database;

import com.charana.login_window.ui.startup.StartUp_Controller;
import com.charana.server.message.ConnectionMessage;
import com.charana.server.message.Message;
import com.charana.server.message.PingMessage;
import com.charana.server.message.database_message.DatabaseResponseMessage;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerConnector {
//    private final InetAddress serverIP;
//    private final int serverPort;
    private static final Logger logger = LoggerFactory.getLogger(ServerConnector.class);
    StartUp_Controller controller;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;
    Thread connect, receive, heartbeat;
    volatile boolean isConnected;//Heartbeat
    LinkedBlockingQueue<DatabaseResponseMessage> databaseResponses = new LinkedBlockingQueue<>();


    final int serverPort;
    final InetAddress serverIP;
    public ServerConnector(InetAddress serverIP, int serverPort, StartUp_Controller controller){
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.controller = controller;
        connectToServer();

        controller.getPrimaryStage().setOnCloseRequest((event) -> {
            disconnect();
        });
    }

    private void connectToServer(){
        connect = new Thread("CONNECT"){
            @Override
            public void run() {
                while(!connect()) { //While not connected (until a connection is established)
                    try { Thread.sleep(1000); } //Wait for 1 second before Re-Connecting
                    catch (InterruptedException e) { logger.error("Current thread interrupted", e); }
                }
                controller.hideWarningDialog();
                logger.info("Client successfully connected to server");
                System.out.println("Screen Swap to Login Email");
            }
        };
        connect.start();
    }

    private boolean connect(){
        try{
            socket = new Socket(serverIP, serverPort);
            try{
                out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                in = new ObjectInputStream(socket.getInputStream());
                isConnected = true;
                send(new ConnectionMessage(null, "Charana", null));
                initHeartBeat();
                initRecieve();
                return true;
            }
            catch (IOException e){
                logger.error("Input/Output stream creation failed (socket closed)", e);
                return false;
            }
        }
        catch (ConnectException e){ //No server running on localhost
            logger.warn("Server not running", e);
            controller.showWarningDialog("Failed to connect to Server", "Check if server is running (on localhost)");
            return false;
        }
        catch (IOException e){ //(Actually a Socket Exception) No Internet Connectivity OR Invalid Port (if localhost)
            //TODO:: Load "no avaiable connection" screen
            return false;
        }
    }

    public boolean DequeueResponse(){
        try{ return databaseResponses.take().result; }
        catch (InterruptedException e){  //If the current thread is interrupted while being blocked
            logger.error("Thread interrupted during de-queue", e);
            return false;
        }
    }

    public void send(Message message){
        try{ //Haven't previously connected to the database (out is still not initialized null)
            if(out == null) { throw new IOException("Haven't connected to database yet"); }
            out.writeObject(message); } //You or Peer has closed the socket connection
        catch (IOException e){ // Log Error sending data (some data is lost to the server) namely `message`
            logger.error("message {} could not be sent to server", new GsonBuilder().create().toJson(message), e);
            //The peer socket closing will be detected & handled by the receive thread (doesn't have to be handled during send)
        }
    }

    private void initHeartBeat(){
        heartbeat = new Thread("HEARTBEAT"){
            @Override
            public void run() {
                while(isConnected){
                    send(new PingMessage(null));
                    try { Thread.sleep(2000); }//Send a PING Message every 2 seconds
                    catch(InterruptedException e) { return; } //If thread is interrupted, terminate by return
                }
            }
        };
        heartbeat.start();
    }

    /**
     * Receive thread
     *  1) handles incoming messages
     *  2) Detects a lost TCP Connection (SocketTimeoutException) and resets connectivity (via disconnect())
     *  3) Detects the peer (server) closing its corresponding socket (EOFException) and resets connectivity
     */
    private void initRecieve(){ //TODO:: Stacks SQLReponses on a stack to be returned by Database messages
        receive = new Thread("RECEIVE"){
            @Override
            public void run() {
                while(isConnected){
                    try{
                        socket.setSoTimeout(5000); //Timeout in 5 seconds
                        Message message = (Message) in.readObject();
                        switch (message.type){
                            case TEXT_MESSAGE:
                                break;
                            case VOICE_MESSAGE:
                                break;
                            case CONNECTED:
                                break;
                            case DISCONNECTED:
                                break;
                            case SERVER:
                                break;
                            case DATABASE_RESPONSE: //Server responses for Database Messages
                                databaseResponses.add((DatabaseResponseMessage) message);
                                break;
                            case PING:
                                System.out.println(String.valueOf(message.type));
                                break;
                        }
                    }
                    //TODO:: Put logger messages here
                    //EOFException is thrown, when peers socket has been closed
                    catch(EOFException e){
                        logger.error("Could not read message from server (peer closed socket)", e);
                        reconnect();
                    }
                    //If we do not get any message (including PING) from the server within 5 seconds
                    //We presume the connection has been lost (internet disconnected/router breakdown)
                    catch (SocketTimeoutException e){
                        logger.error("Lost TCP connection with server (no message in 5 seconds)", e);
                        reconnect(); }
                    //SocketException "Socket Closed" is thrown if you have closed the connection
                    catch (SocketException e){
                        logger.error("Could not read message from server (connection with server previously closed)", e);
                        disconnect();
                    }
                    //Any other TCP issue occurs
                    catch (IOException | ClassNotFoundException e){
                        logger.error("Could not read message from server (unknown cause)", e);
                        reconnect();
                    }
                }
            }
        };
        receive.start();
    }

    public void disconnect(){
        isConnected = false; //Stops RECEIVE and HEARTBEAT threads
        try{ if(socket != null) socket.close(); } //Unblocks any read() call and throws an exception / write() calls throw an Exception
        catch (IOException e){ logger.error("Could not close connection/socket", e); }
        logger.info("Client disconnected from server");

    }

    public void reconnect(){
        disconnect();
        System.out.println("Swap screen to Internet Connectivity Unavailable");
        connectToServer();
    }
}


















