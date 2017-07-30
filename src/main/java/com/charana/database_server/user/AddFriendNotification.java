package com.charana.database_server.user;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable (tableName = "Notifications")
public class AddFriendNotification implements Serializable{

    ProfileImage profileImage;
    DisplayName displayName;
    @DatabaseField (generatedId = true, columnName = "ID")
    Integer id;
    @DatabaseField (columnName = "SourceEmail")
    String sourceEmail;
    @DatabaseField (foreign = true, foreignAutoRefresh = true, columnName = "User_ID")
    User user;

    public AddFriendNotification() {}

    public AddFriendNotification(String sourceEmail, User user){
        this.sourceEmail = sourceEmail;
        this.user = user;
    }

    //GETTERS
    public String getSourceEmail() { return sourceEmail; }

    public ProfileImage getProfileImage() { return profileImage; }

    public DisplayName getDisplayName() { return displayName; }

    //SETTERS
    public void setDisplayName(DisplayName displayName) { this.displayName = displayName; }

    public void setProfileImage(ProfileImage profileImage) { this.profileImage = profileImage; }

}
