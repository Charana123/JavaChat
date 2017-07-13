package com.charana.login_window.ui.create_account;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Test extends Application{

    //Test
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(CreateAccount_Controller.getInstance(null));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) { launch(args); }
}
