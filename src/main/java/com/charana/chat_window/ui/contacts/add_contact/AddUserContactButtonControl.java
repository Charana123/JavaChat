package com.charana.chat_window.ui.contacts.add_contact;

import com.charana.database_server.user.User;
import com.charana.server.message.database_message.Account;
import com.sun.istack.internal.NotNull;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class AddUserContactButtonControl extends HBox implements Initializable {
    @FXML Button contactInformationButton;
    @FXML Button addContactButton;
    private final Consumer<String> onAddHandler;
    private final Account possibleAccount;

    public AddUserContactButtonControl(@NotNull Account possibleAccount, @NotNull Consumer<String> onAddHandler){
        this.possibleAccount = possibleAccount;
        this.onAddHandler = onAddHandler;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/chat_window/contacts/add_contact/AddUserContactControl.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try { fxmlLoader.load(); }
        catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        double padding = 5;

        double addContactButtonRadius = addContactButton.getPrefHeight()/2;
        addContactButton.setClip(new Circle(addContactButtonRadius, addContactButtonRadius, addContactButtonRadius - padding));
        addContactButton.setStyle("-fx-background-color: lightgreen");

        double contactInformationButtonRadius = contactInformationButton.getPrefHeight()/2 - padding;
        ImageView profileImage = new ImageView(new Image(new ByteArrayInputStream(possibleAccount.profileImage.image)));
        profileImage.setFitHeight(contactInformationButtonRadius*2); profileImage.setFitWidth(contactInformationButtonRadius*2);
        profileImage.setClip(new Circle(contactInformationButtonRadius, contactInformationButtonRadius, contactInformationButtonRadius));
        contactInformationButton.setGraphic(profileImage);
        contactInformationButton.setText(possibleAccount.displayName.toString());
        contactInformationButton.setStyle("-fx-background-color: transparent");
    }

    @FXML
    void addContact(){
        String targetEmail = possibleAccount.email;
        onAddHandler.accept(targetEmail);
    }

}
