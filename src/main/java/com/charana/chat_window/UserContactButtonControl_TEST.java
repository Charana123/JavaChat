package com.charana.chat_window;

import com.charana.chat_window.ui.contacts.UserContactButtonControl;
import com.charana.database_server.user.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UserContactButtonControl_TEST extends Application {

    public static void main(String[] args) { launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    Parent createContent() throws Exception{
        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:h2:tcp://localhost:9081/~/database");
        Dao<User,String> userDAO = DaoManager.createDao(connectionSource, User.class);
        User user = userDAO.queryForId("charananandasena@yahoo.com");
        return new UserContactButtonControl(user);
    }

}
