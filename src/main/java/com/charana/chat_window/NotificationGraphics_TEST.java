package com.charana.chat_window;/**
 * Created by Charana on 7/30/17.
 */

import com.charana.chat_window.ui.notification_tab.NewNotificationsGraphic;
import com.charana.chat_window.ui.notification_tab.NoNewNotificationsGraphic;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class NotificationGraphics_TEST extends Application {


    private Parent createContent() {
        VBox root = new VBox();
        Button button = new Button("TEXT");
        button.setGraphic(new NewNotificationsGraphic(25, 25, 8));
        //button.setGraphic(new NoNewNotificationsGraphic(50, 50));
        root.getChildren().add(button);
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
