package com.charana.chat_window;/**
 * Created by Charana on 7/26/17.
 */

import com.charana.chat_window.ui.contacts.add_contact.AddContactSearchBar;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AddContactSearchBar_TEST extends Application {


    private Parent createContent() {
        return new AddContactSearchBar((String searchQuery) -> {return;});
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
