package com.charana.chat_window;


import com.charana.login_window.utilities.database.user.DisplayName;
import com.charana.login_window.utilities.database.user.Status;
import com.charana.login_window.utilities.database.user.User;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;


public class UserSidebarButtonControl extends Button {

    private final double imageRadius;
    private final User user;
    Group statusContainer;

    //TODO:: Give UserSidebarButtonControl a user object (not the individual data)
    UserSidebarButtonControl(double prefWidth, double prefHeight, User user){
        this.user = user;
        imageRadius = (Math.min(prefWidth, prefHeight) - 10)/2;

        //Configure button
        setStyle("-fx-background-color: transparent");
        setPrefSize(prefWidth, prefHeight);
        setText(user.getDisplayName());

        //Load and shape profile image
        ImageView imageView = new ImageView(user.getProfileImage());
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

    void changeStatus(String newStatus){
        Image status = new Image(getClass().getResource("/images/chat_window/status/" + newStatus + ".png").toExternalForm());
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
            if(object1.user.getEmail().equals(object2.user.getEmail())) return true;
        }
        return false;
    }
}
