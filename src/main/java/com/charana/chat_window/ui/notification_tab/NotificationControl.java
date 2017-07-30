package com.charana.chat_window.ui.notification_tab;

import javafx.scene.layout.HBox;

/**
 * Created by Charana on 7/29/17.
 */
public class NotificationControl extends HBox {

    public static final double notificationWidth = 460;
    public static final double notificationHeight = 60;

    public NotificationControl(){
        setPrefSize(notificationWidth, notificationHeight);
    }
}
