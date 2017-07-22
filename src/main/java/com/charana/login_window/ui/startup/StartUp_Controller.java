package com.charana.login_window.ui.startup;

import com.charana.login_window.animations.SkypeLoadingAnimation;
import com.charana.login_window.ui.create_account.CreateAccount_Controller;
import com.charana.login_window.ui.forgot_password.ForgotPassword_Controller;
import com.charana.login_window.ui.login_email.LoginEmail_Controller;
import com.charana.login_window.ui.login_password.LoginPassword_Controller;
import com.charana.login_window.ui.reenter_password.ReenterPassword_Controller;
import com.charana.login_window.utilities.database.DatabaseConnector;
import com.charana.login_window.utilities.database.ServerConnector;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;

public class StartUp_Controller implements Initializable {
    private static Logger logger = LoggerFactory.getLogger(StartUp_Controller.class);
    @FXML VBox mainContainer;
    @FXML VBox contentContainer;
    private Stage primaryStage;
    private InetAddress serverIP;
    private int serverPort;
    public final DatabaseConnector databaseConnector;
    Alert warningDialog;

    public StartUp_Controller(Stage primaryStage, InetAddress serverIP, int serverPort){
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.primaryStage = primaryStage;

        warningDialog = new Alert(Alert.AlertType.WARNING);
        warningDialog.initModality(Modality.NONE);

        ServerConnector serverConnector = new ServerConnector(serverIP, serverPort, this);
        this.databaseConnector = new DatabaseConnector(serverConnector);
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

    public void loadReenterPasswordView(String email){
        Parent root = ReenterPassword_Controller.getInstance(this,  email);
        setContentContainer(root);
    }

    private void setContentContainer(Parent layout){
        contentContainer.getChildren().removeAll(contentContainer.getChildren());
        contentContainer.getChildren().add(layout);
    }

    public void showWarningDialog(String warningHeader, String warningContent){
        if(!warningDialog.isShowing()) {
            Platform.runLater(() -> {
                warningDialog.setTitle("Warning");
                warningDialog.setHeaderText(warningHeader);
                warningDialog.setContentText(warningContent);
                warningDialog.show(); //Non-Blocking
            });
        }
    }

    public void hideWarningDialog(){
        Platform.runLater(() -> {
            if(warningDialog.isShowing()) warningDialog.close();
        });
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

}










