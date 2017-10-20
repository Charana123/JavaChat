package com.charana.chat_window.ui.contacts;

import com.charana.chat_window.ui.main_view.ViewSwapperInterface;
import com.charana.database_server.user.User;
import com.charana.login_window.utilities.database.ServerAPI;
import com.charana.server.message.database_message.Account;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

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
    @FXML ListView<Account> contactsListview;
    ServerAPI dbConnector;
    private List<Account> friends;
    private Account account;

    private ViewSwapperInterface viewSwapper;

    private ContactsMainController(Account account, List<Account> friends, ViewSwapperInterface viewSwapper){
        this.account = account;
        this.friends = friends;
        this.viewSwapper = viewSwapper;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        contactsListview.setItems(FXCollections.observableArrayList(friends));
        contactsListview.setCellFactory(new Callback<ListView<Account>, ListCell<Account>>() {
            @Override
            public ListCell<Account> call(ListView<Account> param) {
                return new FriendListCell();
            }
        });
    }

    private class FriendListCell extends ListCell<Account> {
        @Override
        protected void updateItem(Account item, boolean empty) {
            super.updateItem(item, empty);
            if(!empty){
                setGraphic(new UserContactButtonControl(item, () ->  viewSwapper.loadRecentContact(item)));
            }
        }
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