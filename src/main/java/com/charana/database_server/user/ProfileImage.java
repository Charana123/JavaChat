package com.charana.database_server.user;

import java.io.Serializable;

public class ProfileImage implements Serializable{

    byte[] image;

    public ProfileImage(byte[] image){
        this.image = image;
    }
}
