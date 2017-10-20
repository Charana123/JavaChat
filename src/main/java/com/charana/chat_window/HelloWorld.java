package com.charana.chat_window;

import org.apache.commons.io.FilenameUtils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class HelloWorld {

    public static void main(String[] args) throws Exception{
        System.out.println(HelloWorld.class.getResource(".").toURI().getPath());
        System.out.println(HelloWorld.class.getClassLoader().getResource(".").toURI().getPath());
        System.out.println(HelloWorld.class.getResource("/").toURI().getPath());
        System.out.println(HelloWorld.class.getResource("/views/chat_window/main_view/MainChatView.fxml").toURI().getPath());
        System.out.println("Hello World");
    }
}
