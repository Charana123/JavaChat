package com.charana.login_window.utilities.database.user;

import com.charana.login_window.utilities.database.user.Birthday;
import com.charana.login_window.utilities.database.user.DisplayName;
import com.charana.login_window.utilities.database.user.Gender;
import javafx.scene.image.Image;

public class User {
    Image profileImage; //Loaded/Stored in database as blob
    String email;
    String password;
    DisplayName displayName;
    Status status;
    Gender gender;
    Birthday birthday;

    public User(Image profileImage, String email, String password, DisplayName displayName, Status status, Gender gender, Birthday birthday) {
        this.profileImage = profileImage;
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.status = status;
        this.gender = gender;
        this.birthday = birthday;
    }

    public Image getProfileImage() { return profileImage; }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDisplayName() {
        return displayName.toString();
    }

    public String getStatus() { return status.name(); }

    public String getGender() {
        return gender.name();
    }

    public String getBirthday() {
        return birthday.toString();
    }
}



