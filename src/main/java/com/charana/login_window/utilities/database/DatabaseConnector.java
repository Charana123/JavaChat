package com.charana.login_window.utilities.database;

import com.charana.database_server.user.User;
import com.charana.server.message.Message;
import com.charana.server.message.database_message.database_command_messages.concrete_database_command_messages.*;
import com.charana.server.message.database_message.database_response_messages.concrete_database_response_messages.GetAccountResponseMessage;
import com.charana.server.message.database_message.database_response_messages.concrete_database_response_messages.GetFriendsResponseMessage;

import java.util.List;


public class DatabaseConnector {
    ServerConnector serverConnector;

    public DatabaseConnector(ServerConnector serverConnector) {
        this.serverConnector = serverConnector;
    }

    public boolean login(String email, String password) {
        serverConnector.send(new LoginMessage(null, email, password));
        return serverConnector.DequeueResponse().success;
    }

    public boolean accountExists(String email){
        serverConnector.send(new AccountExistsMessage(null, email));
        return serverConnector.DequeueResponse().success;
    }

    public boolean resetPassword(String email, String newPassword){
        serverConnector.send(new ResetPasswordMessage(null, email, newPassword));
        return serverConnector.DequeueResponse().success;
    }

    public boolean createAccount(User user){
        serverConnector.send(new CreateAccountMessage(null, user));
        return serverConnector.DequeueResponse().success;
    }

    public User getAccount(String email){
        serverConnector.send(new GetAccountMessage(null, email));
        return ((GetAccountResponseMessage) serverConnector.DequeueResponse()).user;
    }

    public List<User> getFriends(String email){
        serverConnector.send(new GetFriendsMessage(null, email));
        return ((GetFriendsResponseMessage) serverConnector.DequeueResponse()).friends;
    }

    //TODO:: Set a onCompletionHandler, which is passed to the db access function
    //TODO:: The db access function spins a thread that continously keeps trying to send the message to the server until it succeds
    //TODO:: At which point it dequeues the response (if it takes longer than 1 second) it assumed the message wasn't sent back and retries sending
    //TODO:: The call back is then called to update the UI in some way to notify the user

}
