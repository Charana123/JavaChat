package com.charana.database_server.user;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.List;

@DatabaseTable (tableName = "Users")
public class User implements Serializable {
    @DatabaseField (columnName = "Email", dataType = DataType.STRING, id = true)
    private String email;
    @DatabaseField (columnName = "Password", dataType = DataType.STRING)
    private String password;
    @DatabaseField (columnName = "ProfileImage", dataType = DataType.SERIALIZABLE)
    private ProfileImage profileImage;
    @DatabaseField (columnName = "DisplayName", dataType = DataType.SERIALIZABLE)
    private DisplayName displayName;
    @DatabaseField (columnName = "Status", dataType = DataType.ENUM_STRING)
    private Status status;
    @DatabaseField (columnName = "Gender", dataType = DataType.ENUM_STRING)
    private Gender gender;
    @DatabaseField (columnName = "Birthday", dataType = DataType.SERIALIZABLE)
    private Birthday birthday;
    @DatabaseField (columnName = "Friends", dataType = DataType.SERIALIZABLE)
    Friends friends;

    public User() {}

    public static User getInstance(String email, String password, ProfileImage profileImage, DisplayName displayName, Status status, Gender gender, Birthday birthday, Friends friends) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setProfileImage(profileImage);
        user.setDisplayName(displayName);
        user.setStatus(status);
        user.setGender(gender);
        user.setBirthday(birthday);
        user.setFriends(friends);
        return user;
    }

    @Override
    public String toString() {
        return "User{" +
                "profileImage=" + profileImage +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", displayName=" + displayName +
                ", status=" + status +
                ", gender=" + gender +
                ", birthday=" + birthday +
                '}';
    }

    //GETTERS
    public ProfileImage getProfileImage() { return profileImage; }

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

    public Friends getFriends() {
        return friends;
    }

    //SETTERS
    public void setProfileImage(ProfileImage profileImage) { this.profileImage = profileImage; }

    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) { this.password = password; }

    public void setDisplayName(DisplayName displayName) {
        this.displayName = displayName;
    }

    public void setStatus(Status status) { this.status = status; }

    public void setGender(Gender gender) { this.gender = gender; }

    public void setBirthday(Birthday birthday) { this.birthday = birthday; }

    public void setFriends(Friends friends) {
        this.friends = friends;
    }
}



