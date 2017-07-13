package com.charana.login_window.ui.forgot_password;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Test extends Application {


    private Parent createContent() {
        //Parent root = ForgotPassword_Controller.getInstance(null, "charananandasena@yahoo.com");
        return null;
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
