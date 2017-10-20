package com.charana.chat_window;

import com.charana.chat_window.ui.skype_icons.SkypeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class SkypeIconTest extends Application {


    private Parent createContent() {
        VBox root = new VBox();
        root.getChildren().add(new SkypeIcon(FontAwesomeIcon.VIDEO_CAMERA, 40));
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
