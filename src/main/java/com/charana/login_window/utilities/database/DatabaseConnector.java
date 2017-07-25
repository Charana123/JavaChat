package com.charana.login_window.utilities.database;

import com.charana.database_server.user.User;
import com.charana.server.message.database_message.database_command_messages.concrete_database_command_messages.AccountExistsMessage;
import com.charana.server.message.database_message.database_command_messages.concrete_database_command_messages.CreateAccountMessage;
import com.charana.server.message.database_message.database_command_messages.concrete_database_command_messages.LoginMessage;
import com.charana.server.message.database_message.database_command_messages.concrete_database_command_messages.ResetPasswordMessage;


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
