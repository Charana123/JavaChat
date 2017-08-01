package com.charana.chat_window.ui.notification_tab;

import com.charana.login_window.utilities.Procedure;
import com.charana.server.message.database_message.FriendRequest;
import com.charana.server.message.friend_requests.FriendRequestResponseType;
import javafx.scene.control.ListView;
import org.controlsfx.control.PopOver;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class NotificationPopoverControl extends PopOver{

    private final double notificationPaneWidth = NotificationControl.notificationWidth;
    private final double notificationPaneHeight = NotificationControl.notificationHeight * 8; //Height of 8 notifications
    private final double padding = 25;

    public NotificationPopoverControl(List<FriendRequest> friendRequests, Consumer<String> onAcceptFriendRequestHandler, Consumer<String> onRejectFriendRequestHandler){

        ListView<FriendNotificationControl> notificationListView = new ListView<>();
        notificationListView.setStyle("-fx-background-color: white");
        notificationListView.setPrefSize(notificationPaneWidth + padding, notificationPaneHeight);

        List<FriendNotificationControl> friendNotificationControls = friendRequests.stream().map(friendRequest -> {
            return new FriendNotificationControl(
                    friendRequest.sourceAccount.email,
                    friendRequest.sourceAccount.displayName,
                    friendRequest.sourceAccount.profileImage,
                    onAcceptFriendRequestHandler,
                    onRejectFriendRequestHandler,
                    notificationListView,
                    friendRequest.missedNotification);
        }).collect(Collectors.toList());


        notificationListView.getItems().addAll(friendNotificationControls);
        setContentNode(notificationListView);

        setArrowLocation(ArrowLocation.TOP_RIGHT);
    }
}
