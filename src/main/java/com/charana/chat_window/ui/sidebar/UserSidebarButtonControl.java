package com.charana.chat_window.ui.sidebar;


import com.charana.database_server.user.Status;
import com.charana.database_server.user.User;
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

    public UserSidebarButtonControl(double prefWidth, double prefHeight, User user){
        this.email = user.getEmail();
        imageRadius = (Math.min(prefWidth, prefHeight) - 10)/2;

        //Configure button
        setStyle("-fx-background-color: transparent");
        setPrefSize(prefWidth, prefHeight);
        setText(user.getDisplayName().toString());
        setAlignment(Pos.CENTER_LEFT);

        //Load and shape profile image
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(user.getProfileImage().image);
        ImageView imageView = new ImageView(new Image(byteArrayInputStream));
        imageView.setFitHeight(imageRadius*2); imageView.setFitWidth(imageRadius*2);
        Circle clip = new Circle(imageRadius, imageRadius, imageRadius);
        imageView.setClip(clip);

        //Load and shape status image
        statusContainer = new Group();
        changeStatus(user.getStatus());

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
        UserSidebarButtonControl object1 = this;
        if(object1 == obj) return true;
        if(obj instanceof UserSidebarButtonControl){
            UserSidebarButtonControl object2 = (UserSidebarButtonControl) obj;
            if(object1.email.equals(object2.email)) return true;
        }
        return false;
    }
}
