package com.charana.chat_window;

import com.charana.login_window.utilities.database.user.*;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Test extends Application {

    private Parent createContent() {
        VBox vBox = new VBox();

        Image profileImage = new Image(getClass().getResource("/images/chat_window/profile_images/profile1.jpg").toExternalForm());
        User user = new User(profileImage,
                "charananandasena@yahoo.com",
                null,
                new DisplayName("Charana", "Nandasena"),
                Status.ONLINE,
                Gender.MALE,
                new Birthday(7, Month.OCTOBER, 1997));
        UserSidebarButtonControl userSidebarButtonControl = new UserSidebarButtonControl(170,50, user);
        vBox.getChildren().add(userSidebarButtonControl);
        return vBox;
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
