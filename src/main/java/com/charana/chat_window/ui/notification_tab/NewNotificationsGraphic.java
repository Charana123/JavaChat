package com.charana.chat_window.ui.notification_tab;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;


public class NewNotificationsGraphic extends StackPane{
    private Long newNotificationsNum;
    private Button newNotificationsNumButton;

    public NewNotificationsGraphic(double prefWidth, double prefHeight, long newNotificationNum){
        this.newNotificationsNum = newNotificationNum;
        if (!(prefWidth >= prefHeight)) throw new IllegalArgumentException("prefWidth must be equal or larger than prefHeight");
        setPrefSize(prefWidth, prefWidth);

        FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.BELL_ALT);
        iconView.setGlyphSize(prefHeight);
        iconView.setStyle("-fx-fill: white; -fx-text-fill:white;");
        getChildren().add(iconView);
        StackPane.setAlignment(iconView, Pos.BOTTOM_CENTER);

        newNotificationsNumButton = new Button(newNotificationsNum.toString());
        newNotificationsNumButton.setPrefSize(prefWidth/3, prefHeight/3);
        newNotificationsNumButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: " + prefHeight/3 + "px;");
        getChildren().add(newNotificationsNumButton);
        StackPane.setAlignment(newNotificationsNumButton, Pos.TOP_RIGHT);
    }

    public void incrementNotificationNum(){
        newNotificationsNumButton.setText((++newNotificationsNum).toString());
    }
}
