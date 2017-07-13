package com.charana.login_window.ui.create_account;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.controlsfx.control.textfield.CustomPasswordField;
import org.controlsfx.control.textfield.CustomTextField;

public class UnmaskablePasswordField extends VBox {

    private CustomTextField customTextField = new CustomTextField();
    private CustomPasswordField customPasswordField = new CustomPasswordField();

    public UnmaskablePasswordField() {
        //Set Bi-directional binding on `text property`
        customPasswordField.textProperty().bindBidirectional(customTextField.textProperty());
        showTextField(false);

        Label hide = new Label("hide");
        hide.setTextFill(Color.web("#187EB2"));
        hide.setCursor(Cursor.HAND);
        customTextField.setRight(hide);
        hide.setOnMouseClicked((event) -> {
            showTextField(false);
        });

        Label show = new Label("show");
        show.setTextFill(Color.web("#187EB2"));
        show.setCursor(Cursor.HAND);
        customPasswordField.setRight(show);
        show.setOnMouseClicked((event) -> {
            showTextField(true);
        });

        getChildren().addAll(customTextField, customPasswordField);
    }

    private void showTextField(boolean visible){
        customTextField.setVisible(visible);
        customTextField.setManaged(visible);
        customPasswordField.setVisible(!visible);
        customPasswordField.setManaged(!visible);
        if(visible) customTextField.requestFocus();
        else { customPasswordField.requestFocus(); }
    }

    public String getPassword(){
        return customPasswordField.getText();
    }
}
