package com.charana.server;

import com.charana.database_server.user.User;
import com.charana.server.message.*;
import com.charana.server.message.database_message.database_command_messages.*;
import com.charana.server.message.database_message.database_command_messages.concrete_database_command_messages.*;
import com.charana.server.message.database_message.database_response_messages.*;
import com.charana.server.message.database_message.database_response_messages.concrete_database_response_messages.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.support.ConnectionSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.sql.Connection;
import java.util.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Server {
    public final int serverPort = 8192;
    private final Logger logger = LoggerFactory.getLogger(Server.class);
    private boolean serverisRunning;
    //Such that only one client manager thread can be broadcasting a message to all other clients
    private List<ServerClient> clients = Collections.synchronizedList(new ArrayList<>());
    DatabaseConnectionPool connectionPool;

    public Server(InetAddress databaseIP, int databasePort) throws IOException {
        ServerSocket connectionListener = new ServerSocket(serverPort);

        //Start thread (connectionManager) that create threads that manages individual connections with clients
        Thread connectionManager = new Thread("CONNECTION MANAGER") {
            @Override
            public void run() {
                serverisRunning = true;
                while (serverisRunning) {
                    try {
                        Socket connection = connectionListener.accept();
                        Runnable clientConnectionMananger = new clientConnectionManager(connection);
                        new Thread(clientConnectionMananger).start();
                    }
                    catch (InterruptedException e){ //"CONNECTION MANAGER" thread was interrupted
                        logger.warn(Thread.currentThread().getName() + " was Interrupted", e);
                        serverisRunning = false;
                    }
                    catch (IOException e) { //If connecting so a single client connection fails, WARNING + continue waiting for other connections
                        logger.warn("Client connection failed");
                    }
                }
            }
        };
        connectionManager.start();

        //Start with a pool that assumes 5 clients will connect to the server (and thus the database)
        connectionPool = new DatabaseConnectionPool(databaseIP, databasePort, 5);
    }

    //Handles communication with a client, receiving, sending, of messages
    private class clientConnectionManager implements Runnable {
        private final Logger logger = LoggerFactory.getLogger(clientConnectionManager.class);
        private final Socket connection; //Final values can only be set during initialization and in the constructor
        private ObjectOutputStream thisClientOutStream;
        private ServerClient thisClient;
        private boolean hasConnected = false; //Whether client has send a connection message to register with the server
        private String clientName;
        private DatabaseConnectorORM dbconn;

        clientConnectionManager(Socket connection) throws InterruptedException{
            this.connection = connection;
            clientName = connection.getInetAddress().getHostAddress() + ":" + connection.getPort();

            ConnectionSource conn = connectionPool.getConnection();
            dbconn = new DatabaseConnectorORM(conn);
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
                        logger.debug("Message recieved {}", messagefromClient);
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
                    break;
                case DATABASE_COMMAND:
                    processDatabaseCommand((DatabaseCommandMessage) message);
                    break;
                case DISCONNECTED:
                    break;
                case VOICE_MESSAGE:
                    break;
                case SERVER:
                    break;
            }
        }


        private void processDatabaseCommand(DatabaseCommandMessage message){
            boolean result;
            switch (message.commandType){
                case LOGIN:
                    LoginMessage loginMessage = (LoginMessage) message;
                    result = dbconn.login(loginMessage.email, loginMessage.password);
                    send(new DatabaseResponseMessage(null, result));
                    break;
                case CREATE_ACCOUNT:
                    CreateAccountMessage createAccountMessage = (CreateAccountMessage) message;
                    System.out.println(" createAccountMessage RECIEVED");
                    result = dbconn.createAccount(createAccountMessage.user);
                    send(new DatabaseResponseMessage(null, result));
                    break;
                case ACCOUNT_EXISTS:
                    AccountExistsMessage accountExistsMessage = (AccountExistsMessage) message;
                    System.out.println("accountExistsMessage RECIEVED");
                    result = dbconn.accountExists(accountExistsMessage.email);
                    System.out.println(result);
                    send(new DatabaseResponseMessage(null, result));
                    break;
                case RESET_PASSWORD:
                    ResetPasswordMessage resetPasswordMessage = (ResetPasswordMessage) message;
                    result = dbconn.resetPassword(resetPasswordMessage.email, resetPasswordMessage.newPassword);
                    send(new DatabaseResponseMessage(null, result));
                    break;
                case GET_ACCOUNT:
                    GetAccountMessage getAccountMessage = (GetAccountMessage) message;
                    User user = dbconn.getAccount(getAccountMessage.email);
                    send(new GetAccountResponseMessage(null, (user != null), user));
                    break;
                case GET_FRIENDS:
                    GetFriendsMessage getFriendsMessage = (GetFriendsMessage) message;
                    List<User> friends = dbconn.getFriends(getFriendsMessage.email);
                    send(new GetFriendsResponseMessage(null, (friends != null), friends));
                    break;
            }
        }

        private void sendToAll(TextMessage text_message){
            logger.info("Client: " + thisClient.name + " sending text message: " + text_message);
            //Such that no modifications occur to the client list (add or remove) during traversal/iteration in the below code
            synchronized (clients){
                clients.stream().forEach(client -> {
                    try { //The client manager that is broadcasting a message to every client synchronizes with the client manager
                        synchronized (client.toClientStream){ //thread that owns the client
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
            try { //The client manager that is broadcasting a message to every client synchronizes with the client manager
                synchronized (thisClientOutStream){ //thread that owns the client
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
            connectionPool.closeConnection(dbconn.conn);
            logger.info("Closing connection with client {}:{}",  connection.getInetAddress().getHostAddress(), connection.getPort());
            try { connection.close(); }
            catch (IOException e) { logger.error("Could not close connection/socket", e); }
        }
    }


    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger("Server (main)");
        if(args.length == 2){
            try {
                InetAddress databaseIP = InetAddress.getByName(args[0]);
                int databasePort = Integer.parseInt(args[1]);
                new Server(databaseIP, databasePort);
            }
            catch (UnknownHostException e){
                System.out.println("Enter valid database IP Address i.e. \"locahost\"");
                System.exit(1);
            }
            catch (NumberFormatException e){
                System.out.println("Enter valid database ephemeral port i.e. 9081");
                System.exit(1);
            }
            catch (IOException e) {
                logger.error("Server creation failed", e);
                System.exit(1);
            }
        }
        else { System.out.println("java -jar server.jar [databaseIP :: String] [databasePort :: int]"); }
    }
}

