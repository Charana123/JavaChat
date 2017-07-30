package com.charana.login_window.ui.create_account;

import com.charana.login_window.ui.BaseController;
import com.charana.login_window.ui.startup.StartUp_Controller;
import com.charana.database_server.user.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CreateAccount_Controller extends BaseController implements Initializable {

    Logger logger = LoggerFactory.getLogger(CreateAccount_Controller.class);
    @FXML TextField firstNameField;
    @FXML TextField lastNameField;
    @FXML TextField emailAddressField;
    @FXML VBox passwordFieldContainer;
    UnmaskablePasswordField passwordField;
    @FXML ComboBox<String> birthMonthChooser;
    @FXML TextField birthDay;
    @FXML TextField birthYear;
    @FXML ComboBox<String> genderChooser;
    Optional<Region> firstEmptyFieldOptional = Optional.empty(); //Polymorphism FTW

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Populate ComboBoxes
        Gender[] genders = Gender.MALE.getDeclaringClass().getEnumConstants();
        Month[] months = Month.JANUARY.getDeclaringClass().getEnumConstants();
        genderChooser.getItems().addAll(Arrays.asList(genders).stream().map(Gender::name).collect(Collectors.toList()));
        birthMonthChooser.getItems().addAll(Arrays.asList(months).stream().map(Month::name).collect(Collectors.toList()));

        //Inject UnMaskablePasswordField
        passwordField = new UnmaskablePasswordField();
        passwordField.setPrefSize(296, 35);
        passwordField.setLayoutX(27);
        passwordField.setLayoutY(126);
        passwordFieldContainer.getChildren().addAll(passwordField);
    }

    @FXML
    private void createAccount() {
        try {
            String filePath = "/images/chat_window/profile_images/skype-default-avatar.jpg";
            File file = new File(getClass().getResource(filePath).toURI());
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] skypeDefaultImage = IOUtils.toByteArray(fileInputStream);
            String format = FilenameUtils.getExtension(filePath);
            ProfileImage profileImage = new ProfileImage(skypeDefaultImage, format);

            if (fieldsValid()) {
                User user = new User(
                        emailAddressField.getText(),
                        passwordField.getPassword(),
                        profileImage,
                        new DisplayName(firstNameField.getText(), lastNameField.getText()),
                        Status.ONLINE,
                        Gender.valueOf(genderChooser.getValue()),
                        new Birthday(Integer.parseInt(birthDay.getText()), Month.valueOf(birthMonthChooser.getValue()), Integer.parseInt(birthYear.getText())));

                startUp_controller.databaseConnector.accountExists(emailAddressField.getText(), (Boolean exists) -> {
                    if(!exists){
                        startUp_controller.databaseConnector.createAccount(user);
                        logger.info("Account Created");
                        this.startUp_controller.loadLoginEmailView();
                    }
                    else { logger.info("Account Exists");}
                });
            }
        } catch (URISyntaxException | IOException e){
            logger.error("Failure loading default skype avatar", e);
        }
    }

    @FXML
    private void cancel(){
        this.startUp_controller.loadLoginEmailView();
    }

    private boolean fieldsValid(){
        firstEmptyFieldOptional.ifPresent(firstEmptyField -> {
            if(firstEmptyField instanceof TextField) firstEmptyField.setStyle("-fx-border-color: transparent transparent #187EB2 transparent;");
            if(firstEmptyField instanceof ComboBox) firstEmptyField.setStyle("-fx-border-color: transparent");
        });

        List<Region> fields = Arrays.asList(firstNameField, lastNameField, emailAddressField, passwordField, birthMonthChooser, birthDay, birthYear, genderChooser);
        Optional<Region> emptyFieldOptional = fields.stream().filter(field -> {
            if(field instanceof TextField) { return ((TextField) field).getText().isEmpty(); }
            if(field instanceof ComboBox) { return ((ComboBox<String>) field).getValue() == null; }
            return false;
        }).findFirst();
        if(emptyFieldOptional.isPresent()){
            Region emptyField = emptyFieldOptional.get();
            emptyField.setStyle("-fx-border-color: red;");
            firstEmptyFieldOptional = emptyFieldOptional;
            return false;
        }
        return true;
    }

    public static Parent getInstance(StartUp_Controller mainController){
        try{
            FXMLLoader loader = new FXMLLoader(CreateAccount_Controller.class.getResource("/views/login_window/CreateAccount_View.fxml"));
            Parent root = loader.load();
            BaseController controller = loader.getController();
            controller.initStartUpController(mainController);
            return root;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}










