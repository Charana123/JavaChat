package com.charana.chat_window;

import com.charana.chat_window.ui.contacts.UserContactButtonControl;
import com.charana.database_server.user.DisplayName;
import com.charana.database_server.user.User;
import com.charana.login_window.utilities.database.DatabaseConnector;
import com.charana.login_window.utilities.database.ServerConnector;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.net.InetAddress;
import java.util.List;

public class UserContactButtonControl_TEST extends Application {

    public static void main(String[] args) { launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception {

        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:h2:tcp://localhost:9081/~/Desktop/Application/database");
        InetAddress localhost = InetAddress.getByName("localhost");
        DatabaseConnector dbConnector = new DatabaseConnector(new ServerConnector(localhost, 8192));
        dbConnector.getPossibleUsers(new DisplayName("Charana", null), (Boolean success, List<User> possibleUsers) -> {
            User user = possibleUsers.get(0);
            UserContactButtonControl userContactButtonControl = new UserContactButtonControl(user);
            primaryStage.setScene(new Scene(userContactButtonControl));
            primaryStage.show();
        });
//        dbConnector.getFriends("charananandasena@yahoo.com", (Boolean success, List<User> friends) -> {
//            User user = friends.get(0);
//            UserContactButtonControl userContactButtonControl = new UserContactButtonControl(user);
//            primaryStage.setScene(new Scene(userContactButtonControl));
//            primaryStage.show();
//        });


    }
}
