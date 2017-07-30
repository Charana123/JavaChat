package com.charana.chat_window.ui.notification_tab;

import com.charana.database_server.user.DisplayName;
import com.charana.database_server.user.ProfileImage;
import com.charana.database_server.user.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class FriendNotificationControl extends NotificationControl implements Initializable{
    private DisplayName sourceDisplayName;
    private ProfileImage sourceProfileImage;
    @FXML Button acceptButton;
    @FXML Button rejectButton;
    @FXML Button contentButton;

    public FriendNotificationControl(DisplayName sourceDisplayName, ProfileImage sourceProfileImage){
        this.sourceDisplayName = sourceDisplayName;
        this.sourceProfileImage = sourceProfileImage;

        FXMLLoader fxmlLoader = new FXMLLoader(FriendNotificationControl.class.getResource("/views/chat_window/notifications/FriendNotification.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        try { fxmlLoader.load(); }
        catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        contentButton.setText(sourceDisplayName.toString());
        contentButton.setAlignment(Pos.CENTER_LEFT);

        ImageView profileImage = new ImageView(new Image(new ByteArrayInputStream(sourceProfileImage.image)));
        double imageDimension = NotificationControl.notificationHeight - 10;
        profileImage.setFitWidth(imageDimension);
        profileImage.setFitHeight(imageDimension);
        profileImage.setClip(new Rectangle(0, 0, imageDimension, imageDimension));
        contentButton.setGraphic(profileImage);
    }
}
