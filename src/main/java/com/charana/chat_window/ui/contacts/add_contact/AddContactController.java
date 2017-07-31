package com.charana.chat_window.ui.contacts.add_contact;

import com.charana.database_server.user.DisplayName;
import com.charana.database_server.user.User;
import com.charana.login_window.utilities.database.ServerAPI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AddContactController implements Initializable {

    @FXML VBox addContactSearchBarContainer;
    @FXML ListView<AddUserContactButtonControl> searchResultsListview;
    private final ServerAPI serverAPI;
    private final User user;


    private AddContactController(ServerAPI dbConnector, User user){
        this.serverAPI = dbConnector;
        this.user = user;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Consumer<String> onAddHandler = (String targetEmail) -> {
            System.out.println("Sending Friend Request " + user.getEmail() + " " + targetEmail);
            serverAPI.sendFriendRequestMessage(user.getEmail(), targetEmail);
        };
        addContactSearchBarContainer.getChildren().add(new AddContactSearchBar((String searchQuery) -> {
            String emailREGEXP = "^[\\w_.%+-]+@[\\w.-]+\\.[\\w^\\d]{2,6}$";
            if(Pattern.matches(emailREGEXP, searchQuery)){
                serverAPI.getAccount(searchQuery, (Boolean success, User user) -> {
                    if(success){
                        updateSearchResults(Arrays.asList(new AddUserContactButtonControl(user, onAddHandler)));
                    }
                    else clearSearchResults();
                });
            }
            else {
                String[] fullName = searchQuery.split(" ");
                DisplayName displayName = new DisplayName(null, null);
                if(fullName.length == 1) { displayName = new DisplayName(fullName[0], null); }
                else if (fullName.length >= 2) { displayName = new DisplayName(fullName[0], fullName[1]); }
                serverAPI.getPossibleUsers(displayName, (Boolean success, List<User> possibleUsers) -> {
                    if(success){
                        List<AddUserContactButtonControl> addUserContactButtonControls = possibleUsers.stream().map(possibleUser -> new AddUserContactButtonControl(possibleUser, onAddHandler)).collect(Collectors.toList());
                        updateSearchResults(addUserContactButtonControls);
                    }
                    else clearSearchResults();
                });
            }
        }));
    }

    private void clearSearchResults(){
        searchResultsListview.getItems().removeAll(searchResultsListview.getItems());
    }

    private void updateSearchResults(List<AddUserContactButtonControl> addUserContactButtonControls){
        searchResultsListview.getItems().removeAll(searchResultsListview.getItems());
        searchResultsListview.getItems().addAll(addUserContactButtonControls);
    }

    public static Parent getInstance(ServerAPI dbConnector, User user){
        FXMLLoader fxmlLoader = new FXMLLoader(AddContactController.class.getResource("/views/chat_window/contacts/add_contact/AddContactView.fxml"));
        fxmlLoader.setController(new AddContactController(dbConnector, user));
        try { return fxmlLoader.load(); }
        catch (IOException e) { e.printStackTrace(); return null; }
    }

}










