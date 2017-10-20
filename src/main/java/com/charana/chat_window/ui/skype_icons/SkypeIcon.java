package com.charana.chat_window.ui.skype_icons;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SkypeIcon extends Group {

    public Button button;
    public FontAwesomeIconView iconView;

    public SkypeIcon(FontAwesomeIcon icon, double dimensionLength){
        button = new Button();
        button.setPrefSize(dimensionLength, dimensionLength);
        button.styleProperty().bind(Bindings
                .when(button.hoverProperty())
                .then(new SimpleStringProperty("-fx-background-color: #1DB0ED"))
                .otherwise(new SimpleStringProperty("-fx-background-color: white")));

        iconView = new FontAwesomeIconView(icon);
        iconView.setGlyphSize(dimensionLength - dimensionLength/2);
        iconView.styleProperty().bind(Bindings
                .when(button.hoverProperty())
                .then(new SimpleStringProperty("-fx-fill: white"))
                .otherwise(new SimpleStringProperty("-fx-fill: #1DB0ED")));
        button.setGraphic(iconView);

        double radius = dimensionLength/2;
        button.setClip(new Circle(radius, radius, radius));

        getChildren().add(button);
    }

}
