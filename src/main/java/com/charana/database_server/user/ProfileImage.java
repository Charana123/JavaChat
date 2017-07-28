package com.charana.database_server.user;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.Serializable;

public class ProfileImage implements Serializable{

    final byte[] image;

    public ProfileImage(byte[] image){
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }
}
