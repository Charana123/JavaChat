package com.charana.chat_window.ui.notification_tab;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.layout.StackPane;

public class NoNewNotificationsGraphic extends StackPane {

    public NoNewNotificationsGraphic(double prefWidth, double prefHeight){
        if (!(prefWidth >= prefHeight)) throw new IllegalArgumentException("prefWidth must be equal or larger than prefHeight");
        setPrefSize(prefWidth, prefHeight);

        FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.BELL_ALT);
        iconView.setGlyphSize(prefHeight- prefHeight/8);
        getChildren().add(iconView);
    }
}
