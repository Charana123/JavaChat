package com.charana.chat_window.ui.conversation;

import com.charana.server.message.database_message.Account;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.swing.text.html.ListView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConversationWindowController implements Initializable{
    @FXML HBox contactSummaryContainer;
    @FXML HBox conversationBoxContainer;
    @FXML ListView conversation;
    private final Account account;


    public ConversationWindowController(Account account){
        this.account = account;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        contactSummaryContainer.getChildren().add(new ContactSummary(account));
        conversationBoxContainer.getChildren().add(new ConversationBox());
        populateConversation();
    }

    void populateConversation(){

    }

    public static Parent getInstance(Account account) {
        ///views/chat_window/contacts/ContactsMainView.fxml
        FXMLLoader loader = new FXMLLoader(ConversationWindowController.class.getResource("/views/chat_window/conversation/ConversationWindow.fxml"));
        loader.setController(new ConversationWindowController(account));
        try{ return loader.load(); }
        catch (IOException e) { e.printStackTrace(); return null; }
    }
}
