package com.charana.database_server.user;

import com.charana.server.message.database_message.DisplayName;
import com.charana.server.message.database_message.ProfileImage;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable (tableName = "Users")
public class User implements Serializable {
    @DatabaseField (columnName = NAME_FIELD_COLUMN_NAME, dataType = DataType.STRING, id = true)
    private String email;
    @DatabaseField (columnName = PASSWORD_FIELD_COLUMN_NAME, dataType = DataType.STRING)
    private String password;
    @DatabaseField (columnName = PROFILE_IMAGE_METADATA_FIELD_COLUMN_NAME, dataType = DataType.STRING)
    private String profileImageMetaData;
    @DatabaseField (columnName = STATUS_FIELD_COLUMN_NAME, dataType = DataType.ENUM_STRING)
    private Status status;
    @DatabaseField (columnName = FIRST_NAME_COLUMN_NAME, dataType = DataType.STRING)
    private String firstName;
    @DatabaseField (columnName = LAST_NAME_COLUMN_NAME, dataType = DataType.STRING)
    private String lastName;
    @DatabaseField (columnName = GENDER_FIELD_COLUMN_NAME, dataType = DataType.ENUM_STRING)
    private Gender gender;
    @DatabaseField (columnName = BIRTHDAY_FIELD_COLUMN_NAME, dataType = DataType.STRING)
    private String birthday;
    @ForeignCollectionField (eager = false)
    private ForeignCollection<Friend> friends;
    @ForeignCollectionField (eager = false)
    private ForeignCollection<AddFriendNotification> addFriendNotification;


    public static final String NAME_FIELD_COLUMN_NAME = "Email";
    public static final String PASSWORD_FIELD_COLUMN_NAME = "Password";
    public static final String PROFILE_IMAGE_METADATA_FIELD_COLUMN_NAME = "ProfileImageMetaData";
    public static final String FIRST_NAME_COLUMN_NAME = "firstName";
    public static final String LAST_NAME_COLUMN_NAME = "lastName";
    public static final String STATUS_FIELD_COLUMN_NAME = "Status";
    public static final String GENDER_FIELD_COLUMN_NAME = "Gender";
    public static final String BIRTHDAY_FIELD_COLUMN_NAME = "Birthday";

    public User() {}

    public User(String email){
        this.email = email;
    }

    public User(String email, String password, String profileImageMetaData, DisplayName displayName, Status status, Gender gender, Birthday birthday) {
        this.email = email;
        this.password = password;
        this.profileImageMetaData = profileImageMetaData;
        this.firstName = displayName.firstName;
        this.lastName = displayName.lastName;
        this.status = status;
        this.gender = gender;
        this.birthday = birthday.toString();
    }

    //GETTERS
    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public Status getStatus() { return status; }

    public Gender getGender() { return gender; }

    public String getBirthday() { return birthday; }

    public String getLastName() { return lastName; }

    public String getFirstName() { return firstName; }

    public ForeignCollection<Friend> getFriends() { return friends; }

    public String getProfileImageMetaData() { return profileImageMetaData; }

    public ForeignCollection<AddFriendNotification> getAddFriendNotification() { return addFriendNotification; }


    @Override
    public boolean equals(Object obj){
        User user1 = this;
        if(user1 == obj) return true;
        else if(obj instanceof User){
            User user2 = (User) obj;
            if(user1.hashCode() == user2.hashCode()) return true;
        }
        return false;
    }


    @Override
    public int hashCode() {
        return email.hashCode();
    }
}



