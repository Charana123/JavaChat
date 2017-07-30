package com.charana.database_server.user;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.Serializable;

public class ProfileImage implements Serializable{

    public final byte[] image;
    public final String format;

    public ProfileImage(byte[] image, String extension){
        this.image = image;
        this.format = extension;
    }

}
