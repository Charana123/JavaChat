package com.charana.chat_window;/**
 * Created by Charana on 7/28/17.
 */

import com.charana.chat_window.ui.contacts.UserContactButtonControl;
import com.charana.chat_window.ui.contacts.add_contact.AddUserContactButtonControl;
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
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import java.net.InetAddress;
import java.sql.SQLException;
import java.util.List;

public class AddUserContactButtonControl_TEST extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:h2:tcp://localhost:9081/~/Desktop/Application/database");
        InetAddress localhost = InetAddress.getByName("localhost");
        DatabaseConnector dbConnector = new DatabaseConnector(new ServerConnector(localhost, 8192));
        dbConnector.getPossibleUsers(new DisplayName("Albie", null), (Boolean success, List<User> users) -> {
            User user = users.get(0);
            AddUserContactButtonControl addUserContactButtonControl = new AddUserContactButtonControl(user, event -> {

            });
            primaryStage.setScene(new Scene(addUserContactButtonControl));
            primaryStage.show();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
