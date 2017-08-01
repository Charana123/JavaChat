package com.charana.chat_window.ui.main_view;

import com.charana.chat_window.ui.contacts.add_contact.AddContactController;
import com.charana.chat_window.ui.notification_tab.FriendNotificationControl;
import com.charana.chat_window.ui.notification_tab.NewNotificationsGraphic;
import com.charana.chat_window.ui.notification_tab.NoNewNotificationsGraphic;
import com.charana.chat_window.ui.notification_tab.NotificationPopoverControl;
import com.charana.chat_window.ui.sidebar.UserSidebarButtonControl;
import com.charana.chat_window.ui.contacts.ContactsMainController;
import com.charana.database_server.user.AddFriendNotification;
import com.charana.database_server.user.User;
import com.charana.login_window.BaseWindowController;
import com.charana.login_window.utilities.database.ServerAPI;
import com.charana.login_window.utilities.database.ServerConnector;
import com.charana.server.message.database_message.Account;
import com.charana.server.message.database_message.FriendRequest;
import com.charana.server.message.friend_requests.FriendRequestResponseType;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ChatController extends BaseWindowController implements Initializable, ViewSwapperInterface, ServerUIInterface {

    @FXML VBox contentContainer;

    @FXML ListView<UserSidebarButtonControl> recentContacts;
    @FXML Button notificationsButton;
    ObservableList<UserSidebarButtonControl> recentContactsItemsView;
    ArrayList<UserSidebarButtonControl> userecentContactsItems;
    private final ServerConnector serverConnector;
    private final ServerAPI serverAPI;
    private final Stage chatStage;
    private Account account;


    private ChatController(Stage chatStage, Account account, InetAddress serverIP, int serverPort){
        super();
        this.serverConnector = new ServerConnector(
                serverIP,
                serverPort,
                () -> hideWarningDialog(),
                (String warningHeader, String warningContent) -> showWarningDialog(warningHeader, warningContent),
                this,
                account.email);
        chatStage.setOnCloseRequest(event -> serverConnector.disconnect());
        this.serverAPI = new ServerAPI(serverConnector);
        this.chatStage = chatStage;
        this.account = account;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Anything code prior to scene populating (attaching listeners, loading data from model)
        recentContactsItemsView = recentContacts.getItems();
        if(account.missedNotifications > 0){
            notificationsButton.setGraphic(new NewNotificationsGraphic(notificationsButton.getPrefWidth()/2, notificationsButton.getPrefHeight()/2, account.missedNotifications));
        }
        else { notificationsButton.setGraphic(new NoNewNotificationsGraphic(notificationsButton.getPrefWidth()/2, notificationsButton.getPrefHeight()/2));}

    }

    @FXML
    void viewContacts(){
        serverAPI.getFriends(account.email, (Boolean success, List<Account> friends) -> {
            Parent root = ContactsMainController.getInstance(account, friends, this);
            swapContent(root);
        });
    }


    @FXML
    void viewFriendNotifications(){
        serverAPI.getAddFriendNotifications(account.email, (Boolean success, List<FriendRequest> friendRequests) -> {
            if(success){
                PopOver popOver = new NotificationPopoverControl(
                        friendRequests,
                        (String friedRequestSourceAccountEmail) -> serverAPI.sendFriendRequestResponseMessage(account.email, friedRequestSourceAccountEmail, FriendRequestResponseType.ACCEPT),
                        (String friedRequestSourceAccountEmail) -> serverAPI.sendFriendRequestResponseMessage(account.email, friedRequestSourceAccountEmail, FriendRequestResponseType.REJECT));
                popOver.setAnimated(false);
                popOver.show(notificationsButton);
            }
        });
        notificationsButton.setGraphic(new NoNewNotificationsGraphic(notificationsButton.getPrefWidth()/2, notificationsButton.getPrefHeight()/2));
    }

    private void swapContent(Parent root){
        contentContainer.getChildren().removeAll(contentContainer.getChildren());
        contentContainer.getChildren().add(root);
    }

    void updateRecentContacts(User user){
        //Specification -
        //List of all friends loaded into recent, When a user is pressed that user goes to the top of the list (and every other user is pushed downwards by one)
        //Implementation -
        //An ArrayList holds all users, when a user is clicked (most recent). This method is called (with a structure of a user)
        //The user is then found in the ArrayList removed and added again (to become to the most recent)
        //The observable list is then cleared and reset with the new ArrayList
    }

    //Interface - ViewSwapperInterface implemented methods
    public void viewAddContact(){
        Parent root = AddContactController.getInstance(serverAPI, account);
        swapContent(root);
    }

    public void loadRecentContact(Account friend){
        UserSidebarButtonControl userSidebarButtonControl = new UserSidebarButtonControl(170, 50, friend);
        recentContacts.getItems().add(userSidebarButtonControl);
    }


    //Interface - ServerUIInterface implemented methods
    @Override
    public void addFriendNotification() {
        Platform.runLater(() -> {
            Node graphicNode = notificationsButton.getGraphic();
            if(graphicNode instanceof NoNewNotificationsGraphic){
                notificationsButton.setGraphic(new NewNotificationsGraphic(notificationsButton.getPrefWidth()/2, notificationsButton.getPrefHeight()/2, 1));
            }
            else if(graphicNode instanceof  NewNotificationsGraphic){
                NewNotificationsGraphic newNotificationsGraphic = (NewNotificationsGraphic) graphicNode;
                newNotificationsGraphic.incrementNotificationNum();
            }
        });
    }



    public static Stage chatWindow(Account account, InetAddress serverIP, int serverPort){
        FXMLLoader fxmlLoader = new FXMLLoader(ChatController.class.getResource("/views/chat_window/main_view/MainChatView.fxml"));
        try {
            Stage chatStage = new Stage();
            fxmlLoader.setController(new ChatController(chatStage, account, serverIP, serverPort));
            Parent root = fxmlLoader.load();
            chatStage.setScene(new Scene(root));
            return chatStage;
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

}










