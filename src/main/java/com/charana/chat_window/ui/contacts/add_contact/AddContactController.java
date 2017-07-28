package com.charana.chat_window.ui.contacts.add_contact;/**
 * Created by Charana on 7/27/17.
 */

import com.charana.chat_window.ui.main_view.ViewSwapper;
import com.charana.login_window.utilities.database.DatabaseConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddContactController implements Initializable {

    @FXML VBox addContactSearchBarContainer;
    @FXML ListView searchResultsListview;
    DatabaseConnector dbConnector;

    private AddContactController(DatabaseConnector dbConnector){
        this.dbConnector = dbConnector;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addContactSearchBarContainer.getChildren().add(new AddContactSearchBar((String searchQuery) -> {
            //create DisplayName and send to server to query for matching instances (via Where, SelectArgs)
            //create Email and send to server to query by id
        }));
    }

    public static Parent getInstance(DatabaseConnector dbConnector){
        FXMLLoader fxmlLoader = new FXMLLoader(AddContactController.class.getResource("/views/chat_window/contacts/add_contact/AddContactView.fxml"));
        fxmlLoader.setController(new AddContactController(dbConnector));
        try { return fxmlLoader.load(); }
        catch (IOException e) { e.printStackTrace(); return null; }
    }

}










