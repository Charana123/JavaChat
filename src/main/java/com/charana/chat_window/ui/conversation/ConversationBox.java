package com.charana.chat_window.ui.conversation;

import com.charana.chat_window.ui.skype_icons.SkypeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.weathericons.WeatherIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConversationBox extends AnchorPane implements Initializable {

    @FXML Pane sendFileButtonContainer;
    @FXML Pane sendVideoButtonContainer;
    @FXML Pane sendEmoticonButtonContainer;
    @FXML TextArea MessageBoxTextArea;

    public ConversationBox(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/chat_window/conversation/ConversationBox.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try { fxmlLoader.load(); }
        catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sendFileButtonContainer.getChildren().add(new SkypeIcon(FontAwesomeIcon.FILE, sendFileButtonContainer.getPrefHeight()));
        sendVideoButtonContainer.getChildren().add(new SkypeIcon(FontAwesomeIcon.VIDEO_CAMERA, sendVideoButtonContainer.getPrefHeight()));
        sendEmoticonButtonContainer.getChildren().add(new SkypeIcon(FontAwesomeIcon.SMILE_ALT, sendEmoticonButtonContainer.getPrefHeight()));
    }


}
