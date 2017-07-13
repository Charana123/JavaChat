package com.charana.login_window.ui.reenter_password;

import com.charana.login_window.ui.BaseController;
import com.charana.login_window.ui.startup.StartUp_Controller;
import com.charana.login_window.utilities.database.SQLiteDatabaseConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import org.controlsfx.control.PopOver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReenterPassword_Controller extends BaseController implements Initializable {

    @FXML PasswordField newPasswordField;
    @FXML PasswordField reenterPasswordField;
    @FXML Button next;
    @FXML Button cancel;
    private static Logger logger = LoggerFactory.getLogger(ReenterPassword_Controller.class);

    PopOver popOver;
    Label popOverContent;
    private String email;

    public ReenterPassword_Controller(String email){
        this.email = email;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popOverContent = new Label();
        popOverContent.setPadding(new Insets(0, 10, 0, 10));
        popOverContent.setStyle("-fx-text-fill: red");
        popOver = new PopOver(popOverContent);
    }

    @FXML
    private void resetPassword(ActionEvent event) {
        String newPassword = newPasswordField.getText();
        if(!newPassword.isEmpty()){
            if(newPassword.equals(reenterPasswordField.getText())){
                if(popOver.isShowing()) popOver.hide();
                boolean resetSuccessful = SQLiteDatabaseConnector.resetPassword(email, newPassword);
                if(resetSuccessful) { logger.debug("Password Reset"); }
                else { logger.warn("Reset Failed"); }
                this.startUp_controller.loadLoginEmailView();
            }
            else{
                popOverContent.setText("Password mismatch");
                popOver.show(reenterPasswordField);
                reenterPasswordField.requestFocus();
            }
        }
        else {
            popOverContent.setText("Empty field");
            popOver.show(newPasswordField);
            newPasswordField.requestFocus();
        }
    }

    @FXML
    private void cancelReset(){
        this.startUp_controller.loadLoginPasswordView(email);
    }

    public static Parent getInstance(StartUp_Controller mainController, String email){
        try {
            FXMLLoader loader = new FXMLLoader(ReenterPassword_Controller.class.getResource("/views/login_window/ReenterPassword_View.fxml"));
            ReenterPassword_Controller controller = new ReenterPassword_Controller(email);
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










