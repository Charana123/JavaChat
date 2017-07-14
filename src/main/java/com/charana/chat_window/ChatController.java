package com.charana.chat_window;

import com.charana.login_window.utilities.database.user.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML ListView<UserSidebarButtonControl> recentContacts;
    ObservableList<UserSidebarButtonControl> recentContactsItemsView;
    ArrayList<UserSidebarButtonControl> userecentContactsItems;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Anything code prior to scene populating (attaching listeners, loading data from model)
        recentContactsItemsView = recentContacts.getItems();

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

}










