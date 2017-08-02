package com.charana.chat_window;

import com.charana.chat_window.ui.contacts.UserContactButtonControl;
import com.charana.server.message.database_message.Account;
import com.charana.server.message.database_message.DisplayName;
import com.charana.database_server.user.User;
import com.charana.login_window.utilities.database.ServerAPI;
import com.charana.login_window.utilities.database.ServerConnector;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.InetAddress;
import java.util.List;

public class UserContactButtonControl_TEST extends Application {

    public static void main(String[] args) { launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception {

        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:h2:tcp://localhost:9081/~/Desktop/Application/database");
        InetAddress localhost = InetAddress.getByName("localhost");
        ServerAPI serverAPI = new ServerAPI(new ServerConnector(localhost, 8192, null, null));
        serverAPI.getPossibleUsers(new DisplayName("Charana", null), (Boolean success, List<Account> possibleAccounts) -> {
            Account account = possibleAccounts.get(0);
            UserContactButtonControl userContactButtonControl = new UserContactButtonControl(account);
            primaryStage.setScene(new Scene(userContactButtonControl));
            primaryStage.show();
        });
    }
}
