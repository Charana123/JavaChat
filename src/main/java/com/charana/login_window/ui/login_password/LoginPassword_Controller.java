package com.charana.login_window.ui.login_password;

import com.charana.login_window.ui.BaseController;
import com.charana.login_window.ui.startup.StartUp_Controller;
import com.charana.login_window.utilities.Emailer;
import com.charana.login_window.utilities.SecureAlphaNumericStringGenerator;
import com.charana.login_window.utilities.database.SQLiteDatabaseConnector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import org.controlsfx.control.PopOver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginPassword_Controller extends BaseController implements Initializable {

    private static Logger logger = LoggerFactory.getLogger(LoginPassword_Controller.class);
    @FXML PasswordField passwordField;
    @FXML Label forgotPassword;
    @FXML Label description;
    private PopOver popOver;

    private String email;

    public LoginPassword_Controller(String email){
        this.email = email;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Anything code prior to scene populating (attaching listeners, loading data from model)
        Label label = new Label("Invalid Password");
        label.setPadding(new Insets(0,10,0,10));
        label.setStyle("-fx-text-fill: red");
        popOver = new PopOver(label);

        String prefix = description.getText();
        description.setText(prefix + " " + email);

        forgotPassword.setOnMouseClicked(event -> {
            String currentAccountKey = SecureAlphaNumericStringGenerator.get();
            String message = "Verification Key: " + currentAccountKey;
            Emailer.sendMessage(email, "Reset Password", message);
            this.startUp_controller.loadForgotPasswordView(currentAccountKey, email);
        });
    }

    @FXML
    private void back(){
        this.startUp_controller.loadLoginEmailView();
    }

    @FXML
    private void login(){
        if(SQLiteDatabaseConnector.login(email, passwordField.getText())){
            if(popOver.isShowing()) popOver.hide();
            logger.debug("Successful Login");
            this.startUp_controller.loadSkypeLoadingAnimation();
            //TODO:: Redirect to Animation, Close StartUp window, Open Chat Window
        }
        else {
            if(!popOver.isShowing()) popOver.show(passwordField);
        }
    }

    public static Parent getInstance(StartUp_Controller mainController, String email){
        try{
            FXMLLoader loader = new FXMLLoader(LoginPassword_Controller.class.getResource("/views/login_window/LoginPassword_View.fxml"));
            LoginPassword_Controller controller = new LoginPassword_Controller(email);
            controller.initStartUpController(mainController);
            loader.setController(controller);
            return loader.load();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}










