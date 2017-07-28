package com.charana.chat_window.ui.contacts;

import com.charana.database_server.user.User;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

import javax.naming.Name;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class UserContactButtonControl extends HBox implements Initializable{
    @FXML Button nameField;
    @FXML Button favouritesField;
    @FXML Button statusField;
    @FXML Button listField;
    @FXML Button locationField;
    private User user;

    public UserContactButtonControl(User user){
        this.user = user;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/chat_window/contacts/UserContactButtonControl.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try{ fxmlLoader.load(); }
        catch (IOException e) { e.printStackTrace(); }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ImageView profilePicture = new ImageView(new Image(new ByteArrayInputStream(user.getProfileImage().getImage())));
        double imageDimension = 40;
        profilePicture.setFitHeight(imageDimension); profilePicture.setFitWidth(imageDimension);
        profilePicture.setClip(new Circle(imageDimension/2, imageDimension/2, imageDimension/2));

        nameField.setGraphic(profilePicture);
        nameField.setText(user.getDisplayName().toString());

        double statusDimension = 20;
        ImageView status = new ImageView(new Image(getClass().getResource("/images/chat_window/status/" + user.getStatus().name() + ".png").toExternalForm()));
        status.setFitWidth(statusDimension); status.setFitHeight(statusDimension);
        status.setClip(new Circle(statusDimension/2, statusDimension/2, statusDimension/2));

        statusField.setGraphic(status);
        statusField.setText("Mood"); //TODO:: `Mood` Field

        listField.setText("--");

        locationField.setText("Location"); //TODO:: `Location` Field
    }

    @FXML
    private void toggleFavourite(){
        FontAwesomeIconView favouriteStatus = (FontAwesomeIconView) favouritesField.getGraphic();
        favouriteStatus.setGlyphName(favouriteStatus.getGlyphName().equals("STAR") ? "STAR_ALT" : "STAR");
    }
}
