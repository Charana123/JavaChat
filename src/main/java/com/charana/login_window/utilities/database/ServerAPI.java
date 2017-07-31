package com.charana.login_window.utilities.database;

import com.charana.database_server.user.AddFriendNotificationDB;
import com.charana.database_server.user.DisplayName;
import com.charana.database_server.user.User;
import com.charana.server.message.Message;
import com.charana.server.message.database_message.database_command_messages.concrete_database_command_messages.*;
import com.charana.server.message.database_message.database_response_messages.DatabaseResponseMessage;
import com.charana.server.message.database_message.database_response_messages.concrete_database_response_messages.GetAccountResponseMessage;
import com.charana.server.message.database_message.database_response_messages.concrete_database_response_messages.GetAddFriendNotificationsResponseMessage;
import com.charana.server.message.database_message.database_response_messages.concrete_database_response_messages.GetFriendsResponseMessage;
import com.charana.server.message.database_message.database_response_messages.concrete_database_response_messages.GetPossibleUsersResponseMessage;
import com.charana.server.message.friend_requests.FriendRequestMessage;
import javafx.application.Platform;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public class ServerAPI {
    ServerConnector serverConnector;

    public ServerAPI(ServerConnector serverConnector) {
        this.serverConnector = serverConnector;
    }

    //DATABASE MESSAGES
    public void login(String email, String password, Consumer<Boolean> completionHandler) {
        new Thread(() -> {
            DatabaseResponseMessage response = sendAndRecieve(new LoginMessage(null, email, password));
            Platform.runLater(() -> completionHandler.accept(response.success));
        }).start();
    }

    public void accountExists(String email, Consumer<Boolean> completionHandler){
        new Thread(() -> {
            DatabaseResponseMessage response = sendAndRecieve(new AccountExistsMessage(null, email));
            Platform.runLater(() -> completionHandler.accept(response.success));
        }).start();
    }

    public void resetPassword(String email, String newPassword, Consumer<Boolean> completionHandler){
        new Thread(() -> {
            DatabaseResponseMessage response = sendAndRecieve(new ResetPasswordMessage(null, email, newPassword));
            Platform.runLater(() -> completionHandler.accept(response.success));
        }).start();
    }

    //Does not require completionHandler
    //Does not require anything (from the response) and therefore does not update anything based on the content of the response
    public void createAccount(User user){
        new Thread(() -> {
            sendAndRecieve(new CreateAccountMessage(null, user));
        }).start();
    }

    public void getAccount(String email, BiConsumer<Boolean, User> completionHandler){
        new Thread(() -> {
            GetAccountResponseMessage response = (GetAccountResponseMessage) sendAndRecieve(new GetAccountMessage(null, email));
            Platform.runLater(() -> completionHandler.accept(response.success, response.user));
        }).start();
    }

    public void getFriends(String email, BiConsumer<Boolean, List<User>> completionHandler){
        new Thread(() -> {
            GetFriendsResponseMessage response = (GetFriendsResponseMessage) sendAndRecieve(new GetFriendsMessage(null, email));
            Platform.runLater(() -> completionHandler.accept(response.success, response.friends));
        }).start();
    }

    public void getPossibleUsers(DisplayName displayName, BiConsumer<Boolean, List<User>> completionHandler){
        new Thread(() -> {
            GetPossibleUsersResponseMessage response = (GetPossibleUsersResponseMessage) sendAndRecieve(new GetPossibleUsersMessage(null, displayName));
            Platform.runLater(() -> completionHandler.accept(response.success, response.possibleUsers));
        }).start();
    }

    public void getAddFriendNotifications(String email, BiConsumer<Boolean, List<AddFriendNotificationDB>> completionHandler){
        new Thread(() -> {
            GetAddFriendNotificationsResponseMessage response = (GetAddFriendNotificationsResponseMessage) sendAndRecieve(new GetAddFriendNotificationsMessage(null , email));
            Platform.runLater(() -> completionHandler.accept(response.success, response.addFriendNotificationDB));
        }).start();
    }

    private DatabaseResponseMessage sendAndRecieve(Message message){
        DatabaseResponseMessage responseMessage;
        //If the message was not sent (true), we don't check to see if anything came (||). So we try sending again
        //If the message was sent (false), check if response came back (||). If we didn't get a response (true). We try sending again.
        //If the message was sent (false), check if response came back (||). If we get a response (false). We exit the loop.
        //Because of the OR Statement (||) both have to evaluate to false for the while loop to exit. Therefore it is guaranteed that
        //response message will be initialized with a non-null message if it exits the loop.
        while(!serverConnector.send(message) || (responseMessage = serverConnector.DequeueResponse()) == null){
            try { Thread.sleep(1000); }
            catch (InterruptedException e) { }
        }
        return responseMessage;
    }


    //OTHER SERVER MESSAGES
    public void sendFriendRequestMessage(String sourceUserEmail, String targetUserEmail){
        sendUntilRecieved(new FriendRequestMessage(null, sourceUserEmail, targetUserEmail));
    }

    private void sendUntilRecieved(Message message){
        new Thread(() -> {
            while(!serverConnector.send(message)) {
                try { Thread.sleep(1000); }
                catch (InterruptedException e){ }
            }
        }).start();
    }


}









