package com.charana.chat_window;/**
 * Created by Charana on 8/5/17.
 */

import com.charana.chat_window.ui.conversation.ConversationBox;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class ConversationBox_TEST extends Application {


    private Parent createContent() {
        Pane root = new Pane();
        root.getChildren().add(new ConversationBox());
        return root;
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
