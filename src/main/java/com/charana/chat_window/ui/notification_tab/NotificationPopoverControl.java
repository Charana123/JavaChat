package com.charana.chat_window.ui.notification_tab;

import com.charana.login_window.utilities.Procedure;
import com.charana.server.message.database_message.FriendRequest;
import com.charana.server.message.friend_requests.FriendRequestResponseType;
import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.controlsfx.control.PopOver;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class NotificationPopoverControl extends PopOver{

    private final double notificationPaneWidth = NotificationControl.notificationWidth;
    private final double notificationPaneHeight = NotificationControl.notificationHeight * 8; //Height of 8 notifications
    private final double padding = 25;

    ListView<FriendRequest> notificationListView;
    Consumer<String> onAcceptFriendRequestHandler;
    Consumer<String> onRejectFriendRequestHandler;

    public NotificationPopoverControl(List<FriendRequest> friendRequests, Consumer<String> onAcceptFriendRequestHandler, Consumer<String> onRejectFriendRequestHandler){
        this.onAcceptFriendRequestHandler = onAcceptFriendRequestHandler;
        this.onRejectFriendRequestHandler = onRejectFriendRequestHandler;

        notificationListView = new ListView<>();
        notificationListView.setStyle("-fx-background-color: white");
        notificationListView.setPrefSize(notificationPaneWidth + padding, notificationPaneHeight);

        notificationListView.setItems(FXCollections.observableArrayList(friendRequests));
        notificationListView.setCellFactory(new Callback<ListView<FriendRequest>, ListCell<FriendRequest>>() {
            @Override
            public ListCell<FriendRequest> call(ListView<FriendRequest> param) {
                return new FriendNotificationListCell();
            }
        });

        setContentNode(notificationListView);
        setArrowLocation(ArrowLocation.TOP_RIGHT);
    }

    private class FriendNotificationListCell extends ListCell<FriendRequest>{
        @Override
        protected void updateItem(FriendRequest item, boolean empty) {
            super.updateItem(item, empty);
            if(!empty){
                FriendNotificationControl friendNotificationControl = new FriendNotificationControl(
                        item,
                        onAcceptFriendRequestHandler.andThen((sourceEmail) -> notificationListView.getItems().remove(item)),
                        onRejectFriendRequestHandler.andThen((sourceEmail) -> notificationListView.getItems().remove(item)),
                        notificationListView,
                        item.missedNotification);
                setGraphic(friendNotificationControl);
            }
        }
    }

}













