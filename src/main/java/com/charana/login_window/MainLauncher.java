package com.charana.login_window;

import com.charana.login_window.ui.startup.StartUp_Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;

public class MainLauncher extends Application {
    static int serverPort;
    static InetAddress serverIP;

    private Parent createContent(Stage primaryStage) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login_window/StartUp_View.fxml"));
        StartUp_Controller controller = new StartUp_Controller(primaryStage, serverIP, serverPort);
        loader.setController(controller);
        return loader.load();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Scene scene = new Scene(createContent(primaryStage));
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        if(args.length != 2) {
            System.out.println("java -jar server.jar [serverIP :: String] [serverPort :: int]");
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
