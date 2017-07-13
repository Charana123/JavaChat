package com.charana.login_window;

import com.charana.login_window.ui.startup.StartUp_Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainLauncher extends Application {

    private Parent createContent(Stage primaryStage) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login_window/StartUp_View.fxml"));
        StartUp_Controller controller = new StartUp_Controller(primaryStage);
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
        launch(args);
    }
}
