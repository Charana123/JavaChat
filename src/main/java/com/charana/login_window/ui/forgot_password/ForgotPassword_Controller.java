package com.charana.login_window.ui.forgot_password;

import com.charana.login_window.ui.BaseController;
import com.charana.login_window.ui.startup.StartUp_Controller;
import com.charana.login_window.utilities.Emailer;
import com.charana.login_window.utilities.SecureAlphaNumericStringGenerator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.controlsfx.control.PopOver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ForgotPassword_Controller extends BaseController implements Initializable {

    @FXML TextField verificationCodeField;
    @FXML Label sendAccountKeyLabel;
    @FXML Label description;
    PopOver popOver;

    private String currentAccountKey;
    private String email;
    private static Logger logger = LoggerFactory.getLogger(ForgotPassword_Controller.class);

    public ForgotPassword_Controller(String currentAccountKey, String email) {
        this.currentAccountKey = currentAccountKey;
        this.email = email;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Anything code prior to scene populating (attaching listeners, loading data from model)
        sendAccountKeyLabel.setOnMouseClicked((event -> resendAccountKey()));
        description.setText("Enter the 6 digit code we sent to " + email);

        Label label = new Label("Incorrect verification key");
        label.setStyle("-fx-text-fill: red");
        popOver = new PopOver(label);
    }

    @FXML
    private void verifyCode() {
        String userAccountKey = verificationCodeField.getText();
        if(userAccountKey.equals(currentAccountKey)){
            if(popOver.isShowing()) popOver.hide();
            logger.warn("AccountKey Verified");
            this.startUp_controller.loadReenterPasswordView(email);
        }
        else { if(!popOver.isShowing()) popOver.show(verificationCodeField); }
    }

    @FXML
    private void resendAccountKey(){
        currentAccountKey = SecureAlphaNumericStringGenerator.get();
        String message = "Verification Key: " + currentAccountKey;
        Emailer.sendMessage(email, "Reset Password", message);
    }

    public static Parent getInstance(StartUp_Controller mainController, String currentAccountKey, String email){
        try {
            FXMLLoader loader = new FXMLLoader(ForgotPassword_Controller.class.getResource("/views/ForgotPassword_View.fxml"));
            ForgotPassword_Controller controller = new ForgotPassword_Controller(currentAccountKey, email);
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










