package com.charana.chat_window.ui.contacts;

import com.charana.chat_window.ui.main_view.ViewSwapper;
import com.charana.database_server.user.User;
import com.charana.login_window.utilities.database.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ContactsMainController implements Initializable {
    @FXML Button allOnlineButton;
    @FXML Button allButton;
    @FXML Button allFavouritesButton;
    @FXML Button addContactButton;
    @FXML ListView contactsListview;
    DatabaseConnector dbConnector;
    private User user;
    private List<User> friends;
    private ViewSwapper viewSwapper;

    private ContactsMainController(User user, List<User> friends, ViewSwapper viewSwapper){
        this.user = user;
        this.friends = friends;
        this.viewSwapper = viewSwapper;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<UserContactButtonControl> userContactButtonControls = friends.stream().map(friend -> new UserContactButtonControl(friend)).collect(Collectors.toList());
        refreshList(userContactButtonControls);
    }

    private void refreshList(List<UserContactButtonControl> userContactButtonControls){
        contactsListview.getItems().removeAll(contactsListview.getItems());
        contactsListview.getItems().addAll(userContactButtonControls);
    }

    public static Parent getInstance(User user, List<User> friends, ViewSwapper viewSwapper){
        FXMLLoader loader = new FXMLLoader(ContactsMainController.class.getResource("/views/chat_window/contacts/ContactsMainView.fxml"));
        loader.setController(new ContactsMainController(user, friends, viewSwapper));
        try { return loader.load(); }
        catch (IOException e) { e.printStackTrace(); return null; }
    }

    @FXML
    void addContact(){
        viewSwapper.viewAddContact();
    }
}

//2 Options
//Option1 -
//You either always use a callback when performing UI operations.
//This way you know you will only perform the UI operation. When you have been provided the data from the database.
//Option2 -
//You initialise all the data you wantat the beginning of the application
//But the problem is whenever the user wants to perform an operation, that data might not have come back yet
//Typically you would error handle this situation forcing the user to get the data before proceeding.
//Such as waiting on the thread that would bring that data.
