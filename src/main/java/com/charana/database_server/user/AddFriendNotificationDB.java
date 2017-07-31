package com.charana.database_server.user;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable (tableName = "Notifications")
public class AddFriendNotificationDB implements Serializable{

    @DatabaseField (generatedId = true, columnName = "ID")
    Integer id;
    @DatabaseField (columnName = "SourceEmail")
    String sourceEmail;
    @DatabaseField (foreign = true, foreignAutoRefresh = true, columnName = "targetUser_ID")
    User targetUser;

    public AddFriendNotificationDB() {}

    public AddFriendNotificationDB(String sourceEmail, User targetUser){
        this.sourceEmail = sourceEmail;
        this.targetUser = targetUser;
    }

    //GETTERS
    public String getSourceEmail() { return sourceEmail; }
    public User getTargetUser() { return targetUser; }

}
