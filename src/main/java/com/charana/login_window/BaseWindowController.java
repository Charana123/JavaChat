package com.charana.login_window;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Modality;

/**
 * Created by Charana on 7/26/17.
 */
public abstract class BaseWindowController {
    protected Alert warningDialog;

    protected BaseWindowController(){
        warningDialog = new Alert(Alert.AlertType.WARNING);
        warningDialog.initModality(Modality.NONE);
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
}
