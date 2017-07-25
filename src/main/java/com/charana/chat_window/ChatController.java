package com.charana.chat_window;

import com.charana.database_server.user.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML ListView<UserSidebarButtonControl> recentContacts;
    ObservableList<UserSidebarButtonControl> recentContactsItemsView;
    ArrayList<UserSidebarButtonControl> userecentContactsItems;
    User user;

    private ChatController(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Anything code prior to scene populating (attaching listeners, loading data from model)
        recentContactsItemsView = recentContacts.getItems();

        //TODO::Make an SQL Query to the Server to get a list of User objects which are Friends of the currently logged individual
        //TODO:: What is the currently logged in individuals email ? but alongside email we should have all his other data
        //TODO:: Should be during login use Constructor DI of the Users User object into the chat controller, which will populate other UI
        //TODO:: elements

        loadRecentContacts();
    }

    //TODO:: Gets a list of friends (from the database). Creates UserSidebarButtonControls for each and populates view.
    //TODO:: Each contact has his own "Friends" table. We can "SELECT * FROM Friends" the entire table.
    void loadRecentContacts(){
        //We have a embedded database. It can hold account and login data but doesn't work when multiple clients are launched from
        //different machines
        //Each account has an assosiated friends table.

        //SO right now we need to swap "SQLite" for a server database like "H2"
    }


    void updateRecentContacts(User user){
        //Specification -
        //List of all friends loaded into recent, When a user is pressed that user goes to the top of the list (and every other user is pushed downwards by one)
        //Implementation -
        //An ArrayList holds all users, when a user is clicked (most recent). This method is called (with a structure of a user)
        //The user is then found in the ArrayList removed and added again (to become to the most recent)
        //The observable list is then cleared and reset with the new ArrayList

    }

    public static Stage chatWindow(){
        FXMLLoader fxmlLoader = new FXMLLoader(ChatController.class.getResource("/views/chat_window/MainLoginView.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Stage chatStage = new Stage();
            chatStage.setScene(new Scene(root));
            return chatStage;
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }


}










