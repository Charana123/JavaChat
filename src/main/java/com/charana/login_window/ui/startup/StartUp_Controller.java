package com.charana.login_window.ui.startup;

import com.charana.chat_window.ui.main_view.ChatController;
import com.charana.database_server.user.User;
import com.charana.login_window.BaseWindowController;
import com.charana.login_window.animations.SkypeLoadingAnimation;
import com.charana.login_window.ui.create_account.CreateAccount_Controller;
import com.charana.login_window.ui.forgot_password.ForgotPassword_Controller;
import com.charana.login_window.ui.login_email.LoginEmail_Controller;
import com.charana.login_window.ui.login_password.LoginPassword_Controller;
import com.charana.login_window.ui.reenter_password.ReenterPassword_Controller;
import com.charana.login_window.utilities.database.ServerAPI;
import com.charana.login_window.utilities.database.ServerConnector;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;

public class StartUp_Controller extends BaseWindowController implements Initializable {
    private static Logger logger = LoggerFactory.getLogger(StartUp_Controller.class);
    @FXML VBox mainContainer;
    @FXML VBox contentContainer;
    private Stage loginStage;
    private InetAddress serverIP;
    private int serverPort;
    public final ServerConnector serverConnector;
    public final ServerAPI serverAPI;


    public StartUp_Controller(Stage loginStage, InetAddress serverIP, int serverPort){
        super();
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.loginStage = loginStage;
        this.serverConnector = new ServerConnector(
                serverIP,
                serverPort,
                (Void voidz) -> hideWarningDialog(),
                (String warningHeader, String warningContent) -> showWarningDialog(warningHeader, warningContent));
        loginStage.setOnCloseRequest(event -> serverConnector.disconnect() );
        this.serverAPI = new ServerAPI(serverConnector);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        loadLoginEmailView();
    }

    public void loadLoginPasswordView(String email){
        Parent root = LoginPassword_Controller.getInstance(this, email);
        setContentContainer(root);
    }

    public void loadLoginEmailView(){
        Parent root = LoginEmail_Controller.getInstance(this);
        setContentContainer(root);
    }

    public void loadForgotPasswordView(String currentAccountKey, String email){
        Parent root = ForgotPassword_Controller.getInstance(this, currentAccountKey, email);
        setContentContainer(root);
    }

    public void loadCreateAccountView(){
        Parent root = CreateAccount_Controller.getInstance(this);
        setContentContainer(root);
    }

    public void loadSkypeLoadingAnimation(){
        SkypeLoadingAnimation root = new SkypeLoadingAnimation();
        VBox vBox = new VBox(root);
        vBox.setPrefHeight(mainContainer.getHeight() - contentContainer.getLayoutX());
        vBox.setAlignment(Pos.CENTER);
        setContentContainer(vBox);
    }

    public void showChatWindow(String email){
        loadSkypeLoadingAnimation();

        //Spawn a non-FX thread that will wait 2 seconds before posting a Runnable that switches windows on the runLater queue
        new Thread(() -> {
            try { Thread.sleep(2000); }
            catch (InterruptedException e) { logger.error("JavaFX Application thread interrupted", e); return; }
            serverAPI.getAccount(email, (Boolean success, User user) -> {
                serverConnector.disconnect();
                Stage chatWindow = ChatController.chatWindow(user, serverIP, serverPort);
                loginStage.hide();
                chatWindow.show();
            });
        }).start();
    }

    public void loadReenterPasswordView(String email){
        Parent root = ReenterPassword_Controller.getInstance(this,  email);
        setContentContainer(root);
    }

    private void setContentContainer(Parent layout){
        contentContainer.getChildren().removeAll(contentContainer.getChildren());
        contentContainer.getChildren().add(layout);
    }

    public Stage getLoginStage() {
        return loginStage;
    }

}










