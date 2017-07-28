package com.charana.database_server.user;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.List;

@DatabaseTable (tableName = "Users")
public class User implements Serializable {
    @DatabaseField (columnName = NAME_FIELD_COLUMN_NAME, dataType = DataType.STRING, id = true)
    private String email;
    @DatabaseField (columnName = PASSWORD_FIELD_COLUMN_NAME, dataType = DataType.STRING)
    private String password;
    @DatabaseField (columnName = PROFILE_IMAGE_FIELD_COLUMN_NAME, dataType = DataType.SERIALIZABLE)
    private ProfileImage profileImage;
    @DatabaseField (columnName = FIRST_NAME_COLUMN_NAME, dataType = DataType.STRING)
    private String firstName;
    @DatabaseField (columnName = LAST_NAME_COLUMN_NAME, dataType = DataType.STRING)
    private String lastName;
    @DatabaseField (columnName = STATUS_FIELD_COLUMN_NAME, dataType = DataType.ENUM_STRING)
    private Status status;
    @DatabaseField (columnName = GENDER_FIELD_COLUMN_NAME, dataType = DataType.ENUM_STRING)
    private Gender gender;
    @DatabaseField (columnName = BIRTHDAY_FIELD_COLUMN_NAME, dataType = DataType.STRING)
    private String birthday;
    @DatabaseField (columnName = FRIENDS_FIELD_COLUMN_NAME, dataType = DataType.SERIALIZABLE)
    Friends friends;


    public static final String NAME_FIELD_COLUMN_NAME = "Email";
    public static final String PASSWORD_FIELD_COLUMN_NAME = "Password";
    public static final String PROFILE_IMAGE_FIELD_COLUMN_NAME = "ProfileImage";
    public static final String FIRST_NAME_COLUMN_NAME = "firstName";
    public static final String LAST_NAME_COLUMN_NAME = "lastName";
    public static final String STATUS_FIELD_COLUMN_NAME = "Status";
    public static final String GENDER_FIELD_COLUMN_NAME = "Gender";
    public static final String BIRTHDAY_FIELD_COLUMN_NAME = "Birthday";
    public static final String FRIENDS_FIELD_COLUMN_NAME = "Friends";


    public User() {}

    public static User getInstance(String email, String password, ProfileImage profileImage, DisplayName displayName, Status status, Gender gender, Birthday birthday, Friends friends) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setProfileImage(profileImage);
        user.setFirstName(displayName.firstName);
        user.setLastName(displayName.lastName);
        user.setStatus(status);
        user.setGender(gender);
        user.setBirthday(birthday.toString());
        user.setFriends(friends);
        return user;
    }

    //GETTERS
    public ProfileImage getProfileImage() { return profileImage; }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Status getStatus() { return status; }

    public Gender getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public Friends getFriends() {
        return friends;
    }

    public DisplayName getDisplayName() { return new DisplayName(firstName, lastName); }

    //SETTERS
    public void setProfileImage(ProfileImage profileImage) { this.profileImage = profileImage; }

    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) { this.password = password; }

    public void setStatus(Status status) { this.status = status; }

    public void setGender(Gender gender) { this.gender = gender; }

    public void setBirthday(String birthday) { this.birthday = birthday; }

    public void setFriends(Friends friends) {
        this.friends = friends;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}



