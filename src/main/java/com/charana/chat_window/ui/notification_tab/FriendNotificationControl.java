package com.charana.chat_window.ui.notification_tab;

import com.charana.server.message.database_message.DisplayName;
import com.charana.server.message.database_message.FriendRequest;
import com.charana.server.message.database_message.ProfileImage;
import com.charana.login_window.utilities.Procedure;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;


public class FriendNotificationControl extends NotificationControl implements Initializable{
    private DisplayName sourceDisplayName;
    private ProfileImage sourceProfileImage;
    private String sourceEmail;
    @FXML Button acceptButton;
    Consumer<String> onAcceptFriendRequestHandler;
    Consumer<String> onRejectFriendRequestHandler;
    @FXML Button rejectButton;
    @FXML Button contentButton;
    private final boolean newNotication;

    public FriendNotificationControl(FriendRequest friendRequest, Consumer<String> onAcceptFriendRequestHandler, Consumer<String> onRejectFriendRequestHandler, ListView<FriendRequest> parentListview, boolean newNotification){
        this.sourceEmail = friendRequest.sourceAccount.email;
        this.sourceDisplayName = friendRequest.sourceAccount.displayName;
        this.sourceProfileImage = friendRequest.sourceAccount.profileImage;
        this.onAcceptFriendRequestHandler = onAcceptFriendRequestHandler;
        this.onRejectFriendRequestHandler = onRejectFriendRequestHandler;
        this.newNotication = newNotification;

        FXMLLoader fxmlLoader = new FXMLLoader(FriendNotificationControl.class.getResource("/views/chat_window/notifications/FriendNotification.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        try { fxmlLoader.load(); }
        catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(newNotication) setStyle("-fx-background-color: #FFFACD");

        contentButton.setText(sourceDisplayName.toString());
        contentButton.setAlignment(Pos.CENTER_LEFT);

        ImageView profileImage = new ImageView(new Image(new ByteArrayInputStream(sourceProfileImage.image)));
        double imageDimension = NotificationControl.notificationHeight - 10;
        profileImage.setFitWidth(imageDimension);
        profileImage.setFitHeight(imageDimension);
        profileImage.setClip(new Rectangle(0, 0, imageDimension, imageDimension));
        contentButton.setGraphic(profileImage);
    }

    @FXML
    void acceptFriendRequest(){
        onAcceptFriendRequestHandler.accept(sourceEmail);
    }

    @FXML
    void rejectFriendRequest(){
        onRejectFriendRequestHandler.accept(sourceEmail);
    }
}
