package com.charana.chat_window.ui.conversation;

import com.charana.chat_window.ui.skype_icons.SkypeIcon;
import com.charana.server.message.database_message.Account;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ContactSummary extends AnchorPane implements Initializable{
    @FXML StackPane userProfileImageContainer;
    @FXML Label userDisplayName;
    @FXML Label userMood;
    @FXML Pane videoCallButtonContainer;
    @FXML Pane createGroupContainer;
    Account account;

    public ContactSummary(Account account){
        this.account = account;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/chat_window/conversation/ContactSummary.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try { fxmlLoader.load(); }
        catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ImageView profileImage = new ImageView(new Image(new ByteArrayInputStream(account.profileImage.image)));
        profileImage.setFitHeight(80);
        profileImage.setFitWidth(80);
        profileImage.setClip(new Circle(40, 40, 40));
        userProfileImageContainer.getChildren().add(profileImage);

        userDisplayName.setText(account.displayName.toString());
        userMood.setText("Mood");

        videoCallButtonContainer.getChildren().add(new SkypeIcon(FontAwesomeIcon.VIDEO_CAMERA, videoCallButtonContainer.getPrefHeight()));
        createGroupContainer.getChildren().add(new SkypeIcon(FontAwesomeIcon.GROUP, createGroupContainer.getPrefHeight()));
    }
}
