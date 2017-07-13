package com.charana.login_window.ui.login_email;


import com.charana.login_window.ui.BaseController;
import com.charana.login_window.ui.startup.StartUp_Controller;
import com.charana.login_window.utilities.database.SQLiteDatabaseConnector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.controlsfx.control.PopOver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginEmail_Controller extends BaseController implements Initializable {


    Logger logger = LoggerFactory.getLogger(LoginEmail_Controller.class);
    @FXML TextField emailField;
    private PopOver popOver;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Label label = new Label("No account exists with the given email");
        label.setPadding(new Insets(0, 10, 0, 10));
        label.setStyle("-fx-text-fill: red");
        popOver = new PopOver(label);
        //Anything code prior to scene populating (attaching listeners, loading data from model)
    }

    @FXML
    void createAccount(){
        this.startUp_controller.loadCreateAccountView();
    }

    @FXML
    void login(){
        String email = emailField.getText();
        boolean accountExists = SQLiteDatabaseConnector.accountExists(email);
        if(accountExists) {
            if(popOver.isShowing()) popOver.hide();
            logger.debug("Account Exists");
            this.startUp_controller.loadLoginPasswordView(email);
        }
        else {
            popOver.show(emailField);
        }
    }

    public static Parent getInstance(StartUp_Controller mainController){
        try{
            FXMLLoader loader = new FXMLLoader(LoginEmail_Controller.class.getResource("/views/LoginEmail_View.fxml"));
            Parent root = loader.load();
            LoginEmail_Controller controller = loader.getController();
            controller.initStartUpController(mainController);
            return root;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}










