package com.charana.login_window.utilities.database.user;

import com.charana.login_window.utilities.database.user.Birthday;
import com.charana.login_window.utilities.database.user.DisplayName;
import com.charana.login_window.utilities.database.user.Gender;

public class User {
    String email;
    String password;
    DisplayName displayName;
    Gender gender;
    Birthday birthday;

    public User(String email, String password, DisplayName displayName, Gender gender, Birthday birthday) {
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.gender = gender;
        this.birthday = birthday;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDisplayName() {
        return displayName.toString();
    }

    public String getGender() {
        return gender.name();
    }

    public String getBirthday() {
        return birthday.toString();
    }
}



