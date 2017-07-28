package com.charana.chat_window;

import com.charana.chat_window.ui.main_view.ChatController;
import com.charana.database_server.user.User;
import com.charana.login_window.utilities.database.DatabaseConnector;
import com.charana.login_window.utilities.database.ServerConnector;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by Charana on 7/26/17.
 */
public class MainChatLauncher extends Application {

    static InetAddress serverIP;
    static int serverPort;

    @Override
    public void start(Stage primaryStage) throws Exception {
        ServerConnector serverConnector = new ServerConnector(serverIP, serverPort);
        DatabaseConnector dbConnector = new DatabaseConnector(serverConnector);
        dbConnector.getAccount("charananandasena@yahoo.com", (Boolean success, User user) -> {
            ChatController.chatWindow(user, serverConnector).show();
        });
    }

    public static void main(String[] args) {
        if(args.length != 2) {
            System.out.println("java -jar client.jar [serverIP :: String] [serverPort :: int]");
            System.exit(1);
        }
        try{
            serverIP = InetAddress.getByName(args[0]);
            serverPort = Integer.parseInt(args[1]);
        } catch (IOException e){
            System.out.println("Enter valid server ip address");
            System.exit(1);
        } catch (NumberFormatException e){
            System.out.println("Enter valid server ephemeral port");
            System.exit(1);
        }
        launch(args);
    }
}
