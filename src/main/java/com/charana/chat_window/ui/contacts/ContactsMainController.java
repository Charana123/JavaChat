package com.charana.chat_window.ui.contacts;

import com.charana.chat_window.ui.main_view.ViewSwapperInterface;
import com.charana.database_server.user.User;
import com.charana.login_window.utilities.database.ServerAPI;
import com.charana.server.message.database_message.Account;
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
    @FXML ListView<UserContactButtonControl> contactsListview;
    ServerAPI dbConnector;
    private Account account;
    private List<Account> friends;
    private ViewSwapperInterface viewSwapper;

    private ContactsMainController(Account account, List<Account> friends, ViewSwapperInterface viewSwapper){
        this.account = account;
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

    public static Parent getInstance(Account account, List<Account> friends, ViewSwapperInterface viewSwapper){
        FXMLLoader loader = new FXMLLoader(ContactsMainController.class.getResource("/views/chat_window/contacts/ContactsMainView.fxml"));
        loader.setController(new ContactsMainController(account, friends, viewSwapper));
        try { return loader.load(); }
        catch (IOException e) { e.printStackTrace(); return null; }
    }

    @FXML
    void addContact(){
        viewSwapper.viewAddContact();
    }
}