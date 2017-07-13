package com.charana.login_window.ui.startup;

import com.charana.login_window.animations.SkypeLoadingAnimation;
import com.charana.login_window.ui.create_account.CreateAccount_Controller;
import com.charana.login_window.ui.forgot_password.ForgotPassword_Controller;
import com.charana.login_window.ui.login_email.LoginEmail_Controller;
import com.charana.login_window.ui.login_password.LoginPassword_Controller;
import com.charana.login_window.ui.reenter_password.ReenterPassword_Controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class StartUp_Controller implements Initializable {
    @FXML VBox mainContainer;
    @FXML VBox contentContainer;
    Stage primaryStage;

    public StartUp_Controller(Stage primaryStage){
        this.primaryStage = primaryStage;
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
}










