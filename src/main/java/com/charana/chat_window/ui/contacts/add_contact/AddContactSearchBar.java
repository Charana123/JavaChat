package com.charana.chat_window.ui.contacts.add_contact;

import com.sun.istack.internal.NotNull;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.controlsfx.control.textfield.CustomTextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;


public class AddContactSearchBar extends HBox implements Initializable {
    @FXML HBox searchBarContainer;
    @FXML CustomTextField searchBar;
    @FXML Button searchBarFocusGlowButton;
    @FXML Button findButton;
    static double growthScale = 5.8;

    static final String borderStyle = "-fx-border-width: 1px;" + "-fx-border-color: grey;" + "-fx-border-radius: 3px;";
    static final String noGlowStyle = "-fx-background-color: transparent;";


    public AddContactSearchBar(@NotNull Consumer<String> onFindHandler){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/chat_window/contacts/add_contact/AddContactSearchBar.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try { fxmlLoader.load(); }
        catch(IOException e) { e.printStackTrace(); }

        findButton.setOnMouseClicked(event -> onFindHandler.accept(searchBar.getText()));
        searchBar.setOnAction(event -> onFindHandler.accept(searchBar.getText()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchBar.setLeft(new FontAwesomeIconView(FontAwesomeIcon.SEARCH));

        searchBarContainer.setOnMouseClicked((event -> searchBar.requestFocus()));
        searchBar.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue == true){
                searchBarFocusGlowButton.setStyle(borderStyle + noGlowStyle);
                KeyValue keyValue1 = new KeyValue(searchBar.layoutXProperty(), searchBar.layoutXProperty().get() - (searchBar.prefWidthProperty().get() * (growthScale - 1)) / 2 );
                KeyValue keyValue2 = new KeyValue(searchBar.prefWidthProperty(), searchBar.prefWidthProperty().get() * growthScale);
                KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.25), keyValue1, keyValue2);
                new Timeline(keyFrame).play();
            } else{
                searchBarFocusGlowButton.setStyle(borderStyle + noGlowStyle);
                KeyValue keyValue1 = new KeyValue(searchBar.layoutXProperty(), searchBar.layoutXProperty().get() + (searchBar.prefWidthProperty().get() * (growthScale-1/growthScale)) / 2 );
                KeyValue keyValue2 = new KeyValue(searchBar.prefWidthProperty(), searchBar.prefWidthProperty().get() * (1/growthScale));
                KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.25), keyValue1, keyValue2);
                new Timeline(keyFrame).play();
            }
        }));

        findButton.disableProperty().bind(searchBar.textProperty().isEmpty());
    }

    @FXML
    void findContact(){
    }


}
