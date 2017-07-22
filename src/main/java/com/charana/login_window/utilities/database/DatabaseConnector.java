package com.charana.login_window.utilities.database;

import com.charana.login_window.utilities.database.user.User;
import com.charana.server.message.database_message.concrete_database_message.AccountExistsMessage;
import com.charana.server.message.database_message.concrete_database_message.CreateAccountMessage;
import com.charana.server.message.database_message.concrete_database_message.LoginMessage;
import com.charana.server.message.database_message.concrete_database_message.ResetPasswordMessage;


public class DatabaseConnector {
    ServerConnector serverConnector;

    public DatabaseConnector(ServerConnector serverConnector) {
        this.serverConnector = serverConnector;
    }

    public boolean login(String email, String password) {
        serverConnector.send(new LoginMessage(null, email, password));
        return serverConnector.DequeueResponse();
    }

    public boolean accountExists(String email){
        serverConnector.send(new AccountExistsMessage(null, email));
        return serverConnector.DequeueResponse();
    }

    public boolean resetPassword(String email, String newPassword){
        serverConnector.send(new ResetPasswordMessage(null, email, newPassword));
        return serverConnector.DequeueResponse();
    }

    public boolean createAccount(User user){
        serverConnector.send(new CreateAccountMessage(null, user));
        return serverConnector.DequeueResponse();
    }

}
