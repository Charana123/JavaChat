package com.charana.login_window.ui.login_password;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Test extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = LoginPassword_Controller.getInstance(null, "charananandasena@yahoo.com");

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){ launch(args); }
}
