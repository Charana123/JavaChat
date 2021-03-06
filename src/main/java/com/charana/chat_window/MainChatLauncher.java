package com.charana.chat_window;

import com.charana.chat_window.ui.main_view.ChatController;
import com.charana.database_server.user.User;
import com.charana.login_window.utilities.database.ServerAPI;
import com.charana.login_window.utilities.database.ServerConnector;
import com.charana.server.message.database_message.Account;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;

public class MainChatLauncher extends Application {

    private static final Logger logger = LoggerFactory.getLogger(MainChatLauncher.class);
    static InetAddress serverIP;
    static int serverPort;

    @Override
    public void start(Stage primaryStage) throws Exception {
        ServerConnector serverConnector = new ServerConnector(serverIP, serverPort, () -> {}, (a, b) -> {});
        ServerAPI serverAPI = new ServerAPI(serverConnector);
        serverAPI.getAccount("cuthbert@gmail.com", (Boolean success, Account account) -> {
            if(success){
                serverConnector.disconnect();
                ChatController.chatWindow(account, serverIP, serverPort).show();
            }
            else { logger.error("Account does not exist"); }
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
