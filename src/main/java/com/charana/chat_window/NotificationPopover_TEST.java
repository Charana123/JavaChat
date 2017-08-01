package com.charana.chat_window;

import com.charana.chat_window.ui.notification_tab.FriendNotificationControl;
import com.charana.chat_window.ui.notification_tab.NotificationPopoverControl;
import com.charana.database_server.user.User;
import com.charana.login_window.utilities.database.ServerAPI;
import com.charana.login_window.utilities.database.ServerConnector;
import com.charana.server.message.database_message.Account;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;

import java.net.InetAddress;
import java.util.Arrays;

public class NotificationPopover_TEST extends Application {


    private Parent createContent(Account account) {
        VBox root = new VBox();
//        Button button = new Button("Click");
//        button.setOnMouseClicked(event -> {
//            PopOver popOver = new NotificationPopoverControl(Arrays.asList(new FriendNotificationControl(account.email, account.displayName, account.profileImage, null, null, true)));
//            popOver.show(button);
//        });
//        root.getChildren().addAll(button);
        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:h2:tcp://localhost:9081/~/Desktop/Application/database");
        InetAddress localhost = InetAddress.getByName("localhost");
        ServerAPI serverAPI = new ServerAPI(new ServerConnector(localhost, 8192, null, null));
        serverAPI.getAccount("charananandasena@yahoo.com", (Boolean success, Account account) -> {
            Scene scene = new Scene(createContent(account));
            primaryStage.setScene(scene);
            primaryStage.show();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
