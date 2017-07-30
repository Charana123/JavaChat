package com.charana.chat_window.ui.main_view;

import com.charana.chat_window.ui.contacts.add_contact.AddContactController;
import com.charana.chat_window.ui.notification_tab.FriendNotificationControl;
import com.charana.chat_window.ui.notification_tab.NotificationPopoverControl;
import com.charana.chat_window.ui.sidebar.UserSidebarButtonControl;
import com.charana.chat_window.ui.contacts.ContactsMainController;
import com.charana.database_server.user.AddFriendNotification;
import com.charana.database_server.user.User;
import com.charana.login_window.BaseWindowController;
import com.charana.login_window.utilities.database.DatabaseConnector;
import com.charana.login_window.utilities.database.ServerConnector;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ChatController extends BaseWindowController implements Initializable, ViewSwapper {

    @FXML VBox contentContainer;

    @FXML ListView<UserSidebarButtonControl> recentContacts;
    @FXML Button notificationsButton;
    ObservableList<UserSidebarButtonControl> recentContactsItemsView;
    ArrayList<UserSidebarButtonControl> userecentContactsItems;
    private final ServerConnector serverConnector;
    private final DatabaseConnector dbConnector;
    private final Stage chatStage;
    private User user;


    private ChatController(Stage chatStage, User user, ServerConnector serverConnector){
        super();
        this.serverConnector = serverConnector;
        serverConnector.setWarningDialogCallbacks(
                (Void voidz) -> hideWarningDialog(),
                (String warningHeader, String warningContent) -> showWarningDialog(warningHeader, warningContent));
        chatStage.setOnCloseRequest(event -> serverConnector.disconnect());
        this.dbConnector = new DatabaseConnector(serverConnector);
        this.chatStage = chatStage;
        this.user = user;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Anything code prior to scene populating (attaching listeners, loading data from model)
        recentContactsItemsView = recentContacts.getItems();
        loadRecentContacts();
    }

    void loadRecentContacts(){ //TODO:: Should this maybe load a different sub-view ?
        dbConnector.getFriends(user.getEmail(), (Boolean success, List<User> friends) -> {
            List<UserSidebarButtonControl> userSidebarButtonControls = friends.stream().map(friend -> new UserSidebarButtonControl(170, 50, friend)).collect(Collectors.toList());
            recentContacts.getItems().addAll(userSidebarButtonControls);
        });
    }

    @FXML
    void viewContacts(){
        dbConnector.getFriends(user.getEmail(), (Boolean success, List<User> friends) -> {
            Parent root = ContactsMainController.getInstance(user, friends, this);
            swapContent(root);
        });
    }

    @FXML
    void viewFriendNotifications(){
        dbConnector.getAddFriendNotifications(user.getEmail(), (Boolean success, List<AddFriendNotification> addFriendNotifications) -> {
            if(success){ //TODO:: Success shoud be changed to a failed DB call, currently if we have 0 notifications, nothing will happen,
                //TODO:: which isn't the definition of a `Successful` database call
                List<FriendNotificationControl> friendNotificationControls = addFriendNotifications.stream().map(addFriendNotification -> {
                    return new FriendNotificationControl(addFriendNotification.getDisplayName(), addFriendNotification.getProfileImage());
                }).collect(Collectors.toList());
                PopOver popOver = new NotificationPopoverControl(friendNotificationControls);
                popOver.setAnimated(false);
                popOver.show(notificationsButton);
            }
        });
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

    //Interface - ViewSwapper implemented methods
    public void viewAddContact(){
        Parent root = AddContactController.getInstance(dbConnector);
        swapContent(root);
    }



    public static Stage chatWindow(User user, ServerConnector serverConnector){
        FXMLLoader fxmlLoader = new FXMLLoader(ChatController.class.getResource("/views/chat_window/main_view/MainChatView.fxml"));
        try {
            Stage chatStage = new Stage();
            fxmlLoader.setController(new ChatController(chatStage, user, serverConnector));
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









