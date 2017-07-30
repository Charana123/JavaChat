package com.charana.chat_window.ui.notification_tab;

import javafx.scene.control.ListView;
import org.controlsfx.control.PopOver;

import java.util.List;

public class NotificationPopoverControl extends PopOver{

    private final double notificationPaneWidth = NotificationControl.notificationWidth;
    private final double notificationPaneHeight = NotificationControl.notificationHeight * 8; //Height of 8 notifications
    private final double padding = 25;

    public NotificationPopoverControl(List<FriendNotificationControl> notifications){
        ListView<NotificationControl> notificationListView = new ListView<>();
        notificationListView.setStyle("-fx-background-color: white");
        notificationListView.setPrefSize(notificationPaneWidth + padding, notificationPaneHeight);
        notificationListView.getItems().addAll(notifications);
        setContentNode(notificationListView);

        setArrowLocation(ArrowLocation.TOP_RIGHT);
    }
}
