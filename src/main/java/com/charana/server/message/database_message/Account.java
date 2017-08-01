package com.charana.server.message.database_message;

import com.charana.database_server.user.*;
import com.charana.server.message.database_message.DisplayName;
import com.charana.server.message.database_message.ProfileImage;

import java.io.Serializable;
import java.util.List;

public class Account implements Serializable{

    //Sent by User (during account creation)
    public final String email;
    public final String password;
    public final ProfileImage profileImage;
    public final Status status;
    public final DisplayName displayName;
    public final Gender gender;
    public final Birthday birthday;

    //Sent by Database
    public List<Friend> friends;
    public List<AddFriendNotification> addFriendNotification;
    public long missedNotifications;

    public Account(String email, String password,  ProfileImage profileImage, Status status, DisplayName displayName, Gender gender, Birthday birthday){
        this.email = email;
        this.password = password;
        this.profileImage = profileImage;
        this.status = status;
        this.displayName = displayName;
        this.gender = gender;
        this.birthday = birthday;

    }


    public Account(String email, String password, ProfileImage profileImage, Status status, DisplayName displayName, Gender gender, Birthday birthday, List<Friend> friends, List<AddFriendNotification> addFriendNotifications, long missedNotifications){
        this(email, password, profileImage, status, displayName, gender, birthday);
        this.friends = friends;
        this.addFriendNotification  = addFriendNotifications;
        this.missedNotifications = missedNotifications;
    }

    @Override
    public String toString() {
        return "Account{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", profileImage=" + profileImage +
                ", status=" + status +
                ", displayName=" + displayName +
                ", gender=" + gender +
                ", birthday=" + birthday +
                '}';
    }
}
