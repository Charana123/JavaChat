package com.charana.server;

import com.charana.database_server.not_persisted_user.FriendRequestDB;
import com.charana.database_server.not_persisted_user.UserData;
import com.charana.database_server.user.*;
import com.charana.server.message.database_message.Account;
import com.charana.server.message.database_message.DisplayName;
import com.charana.server.message.database_message.FriendRequest;
import com.charana.server.message.database_message.ProfileImage;
import com.charana.server.message.*;
import com.charana.server.message.database_message.database_command_messages.*;
import com.charana.server.message.database_message.database_command_messages.concrete_database_command_messages.*;
import com.charana.server.message.database_message.database_response_messages.*;
import com.charana.server.message.database_message.database_response_messages.concrete_database_response_messages.*;
import com.charana.server.message.friend_requests.FriendRequestMessage;
import com.charana.server.message.friend_requests.FriendRequestResponseMessage;
import com.charana.server.message.friend_requests.FriendRequestResponseType;
import com.j256.ormlite.support.ConnectionSource;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Server {
    public final int serverPort = 8192;
    public final String storagePath = "/Users/Charana/Desktop/Application/";
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
                case FRIEND_REQUEST:
                    FriendRequestMessage friendRequestMessage = (FriendRequestMessage) message;
                    boolean success = dbconn.addFriendNotification(friendRequestMessage.sourceUserEmail, friendRequestMessage.targetUserEmail);
                    if(success){ sendToClient(friendRequestMessage.sourceUserEmail, friendRequestMessage.targetUserEmail, friendRequestMessage); }
                    break;
                case FRIEND_REQUEST_RESPONSE:
                    FriendRequestResponseMessage friendRequestResponseMessage = (FriendRequestResponseMessage) message;
                    dbconn.removeFriendNotification(friendRequestResponseMessage.sourceUserEmail, friendRequestResponseMessage.targetUserEmail);
                    if(friendRequestResponseMessage.responseType == FriendRequestResponseType.ACCEPT){
                        dbconn.addFriend(friendRequestResponseMessage.sourceUserEmail, friendRequestResponseMessage.targetUserEmail);
                        dbconn.addFriend(friendRequestResponseMessage.targetUserEmail, friendRequestResponseMessage.sourceUserEmail);
                    }
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
                    User user = accountToUser(createAccountMessage.account);
                    result = dbconn.createUser(user);
                    send(new DatabaseResponseMessage(null, result));
                    break;
                case ACCOUNT_EXISTS:
                    AccountExistsMessage accountExistsMessage = (AccountExistsMessage) message;
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
                    UserData userData = dbconn.getAccount(getAccountMessage.email);
                    if(userData !=  null) {
                        Account account = userToAccount(userData.user, userData.missedNotifications);
                        send(new GetAccountResponseMessage(null, true, account));
                    } else { send(new GetAccountResponseMessage(null, false, null)); }
                    break;
                case GET_FRIENDS:
                    GetFriendsMessage getFriendsMessage = (GetFriendsMessage) message;
                    List<User> friendsUsers = dbconn.getFriends(getFriendsMessage.email);
                    if(friendsUsers !=  null) {
                        List<Account> friendsAccounts = friendsUsers.stream().map(friendUser -> userToAccount(friendUser)).collect(Collectors.toList());
                        send(new GetFriendsResponseMessage(null, true, friendsAccounts));
                    } else { send(new GetFriendsResponseMessage(null, false, null));}
                    break;
                case GET_POSSIBLE_USERS:
                    GetPossibleUsersMessage getPossibleUsersMessage = (GetPossibleUsersMessage) message;
                    List<User> possibleUsers = dbconn.getPossibleUsers(thisClient.name, getPossibleUsersMessage.displayName);
                    if(possibleUsers != null) {
                        List<Account> possibleAccounts = possibleUsers.stream().map(possibleUser -> userToAccount(possibleUser)).collect(Collectors.toList());
                        send(new GetPossibleUsersResponseMessage(null, true, possibleAccounts));
                    } else { send(new GetPossibleUsersResponseMessage(null, false, null)); }
                    break;
                case GET_ADD_FRIEND_NOTIFICATIONS:
                    GetAddFriendNotificationsMessage getAddFriendNotificationsMessage = (GetAddFriendNotificationsMessage) message;
                    List<FriendRequestDB> addFriendNotifications = dbconn.getAddFriendNotifications(getAddFriendNotificationsMessage.email);
                    if(addFriendNotifications != null) {
                        List<FriendRequest> addFriendNotificationAccounts = addFriendNotifications.stream().map(friendNotificationDB -> new FriendRequest(userToAccount(friendNotificationDB.sourceUser), friendNotificationDB.missedNotification)).collect(Collectors.toList());
                        send(new GetAddFriendNotificationsResponseMessage(null, true, addFriendNotificationAccounts));
                    } else { send(new GetAddFriendNotificationsResponseMessage(null, false, null)); }
                    break;
            }
        }

        private Account userToAccount(User user, long missedNotifications){
            ProfileImage profileImage = loadProfileImage(user.getProfileImageMetaData());

            return new Account(
                    user.getEmail(),
                    user.getPassword(),
                    profileImage,
                    user.getStatus(),
                    new DisplayName(user.getFirstName(), user.getLastName()),
                    user.getGender(),
                    Birthday.fromString(user.getBirthday()),
                    missedNotifications);
        }

        private Account userToAccount(User user){
            ProfileImage profileImage = loadProfileImage(user.getProfileImageMetaData());

            return new Account(
                    user.getEmail(),
                    user.getPassword(),
                    profileImage,
                    user.getStatus(),
                    new DisplayName(user.getFirstName(), user.getLastName()),
                    user.getGender(),
                    Birthday.fromString(user.getBirthday()));
        }

        private ProfileImage loadProfileImage(String path){
            try {
                FileInputStream fileInputStream = new FileInputStream(new File(path));
                byte[] profileImage = IOUtils.toByteArray(fileInputStream);
                String format = FilenameUtils.getExtension(path);
                return new ProfileImage(profileImage, format);
            }
            catch (IOException e){
                logger.error("Could not get profileImage (returning empty byte[])", e);
                return new ProfileImage(new byte[]{}, null);
            }
        }

        private User accountToUser(Account account){
            String profileImageMetaData = storeImageReturnPath(account.email, account.profileImage);

            return new User(
                    account.email,
                    account.password,
                    profileImageMetaData,
                    account.displayName,
                    account.status,
                    account.gender,
                    account.birthday
            );
        }

        private String storeImageReturnPath(String sourceEmail, ProfileImage profileImage){
            String username = sourceEmail.split("@")[0];
            String outputPath = storagePath + username + "." + profileImage.format;

            try { //Store Image
                File outputFile = new File(outputPath);
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                fileOutputStream.write(profileImage.image);
            }
            catch (IOException e){
                logger.error("Account '{}' profile image could not be saved", sourceEmail, e);
            }

            return outputPath;
        }

        private void sendToClient(String sourceUserEmail, String targetUserEmail, Message message){
            Optional<ServerClient> targetClientOptional = clients.stream().filter(serverClient -> serverClient.name.equals(targetUserEmail)).findFirst();
            if(targetClientOptional.isPresent()){
                ServerClient targetClient = targetClientOptional.get();
                synchronized (targetClient.toClientStream){
                    try { targetClient.toClientStream.writeObject(message); }
                    catch (IOException e){
                        logger.error("Message type: {} could not be sent to targetUser {} from sourceUser {} (error sending message)", message.type, targetUserEmail, sourceUserEmail, e);
                    }
                }
            }
            else { logger.info("Message type: {} could not be sent to targetUser {} from sourceUser {} (client currently offline)", message.type, targetUserEmail, sourceUserEmail); }
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
                        logger.error("Message {} from sourceClient {} could not be sent to targetClient {}", text_message.text_message, text_message.sourceUserEmail, client.name);
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
                logger.error("Message {} could not be sent to client {} (socket closed)", message.type, clientName, e);
            }
        }

        private void addClient(ConnectionMessage connection_message){
            logger.info("Connection made with client {} ", connection_message.clientName);
            clientName = connection_message.clientName;
            this.thisClient = new ServerClient(clientName, thisClientOutStream, connection);
            clients.add(this.thisClient);
        }

        /**
         * Closing the connection with a client managed by the current thread
         */
        private void closeThisConnection(){
            //Remove the client from the server, if the client has previously connected
            if(!Objects.isNull(thisClient)) { clients.remove(thisClient); }
            connectionPool.closeConnection(dbconn.conn);
            logger.info("Closing connection with client {}",  this.clientName);
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

