package com.charana.login_window.ui.reenter_password;/**
 * Created by Charana on 7/12/17.
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Test extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Scene scene = new Scene(ReenterPassword_Controller.getInstance(null, "charananandasena@yahoo.com"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){ launch(args);}
}
