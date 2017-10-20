package com.charana.chat_window.ui.sidebar;


import com.charana.database_server.user.Status;
import com.charana.database_server.user.User;
import com.charana.server.message.database_message.Account;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

import java.io.ByteArrayInputStream;


public class UserSidebarButtonControl extends Button {

    private final double imageRadius;
    private final String email;
    private Group statusContainer;

    public UserSidebarButtonControl(double prefWidth, double prefHeight, Account account){
        this.email = account.email;
        imageRadius = (Math.min(prefWidth, prefHeight) - 10)/2;

        //Configure button
        setStyle("-fx-background-color: transparent");
        setPrefSize(prefWidth, prefHeight);
        setText(account.displayName.toString());
        setAlignment(Pos.CENTER_LEFT);

        //Load and shape profile image
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(account.profileImage.image);
        ImageView imageView = new ImageView(new Image(byteArrayInputStream));
        imageView.setFitHeight(imageRadius*2); imageView.setFitWidth(imageRadius*2);
        Circle clip = new Circle(imageRadius, imageRadius, imageRadius);
        imageView.setClip(clip);

        //Load and shape status image
        statusContainer = new Group();
        changeStatus(account.status);

        //Add content (profile & status image) to container
        StackPane container = new StackPane(imageView, statusContainer);
        container.setPrefSize(imageRadius*2, imageRadius*2);
        container.setAlignment(Pos.BOTTOM_RIGHT);

        //Add container to button
        setGraphic(container);
    }

    public void changeStatus(Status newStatus){
        Image status = new Image(getClass().getResource("/images/chat_window/status/" + newStatus.name() + ".png").toExternalForm());
        ImageView statusView = new ImageView(status);
        statusView.setFitWidth(imageRadius/1.25); statusView.setFitHeight(imageRadius/1.25);
        Circle statusClip = new Circle((imageRadius/1.25)/2, (imageRadius/1.25)/2, (imageRadius/1.25)/2);
        statusView.setClip(statusClip);
        statusContainer.getChildren().removeAll(statusContainer.getChildren());
        statusContainer.getChildren().add(statusView);
    }

    @Override
    public boolean equals(Object obj) {
        UserSidebarButtonControl obj1 = this;
        if(obj1 == obj) return true;
        if(obj instanceof UserSidebarButtonControl){
            UserSidebarButtonControl obj2 = (UserSidebarButtonControl) obj;
            if(obj1.email.equals(obj2.email)) return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
