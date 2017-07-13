package com.charana.login_window.ui.login_email;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Test extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(LoginEmail_Controller.getInstance(null));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){ launch(args); }
}
