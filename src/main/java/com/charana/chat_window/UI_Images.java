package com.charana.chat_window;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UI_Images extends Application {

    private Parent createContent() throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/views/chat_window/MainLoginView.fxml"));
        return root;
    }

    @Override
    public void start(Stage primaryStage) throws IOException{
        Scene scene = new Scene(createContent());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
