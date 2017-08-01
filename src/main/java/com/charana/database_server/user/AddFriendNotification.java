package com.charana.database_server.user;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable (tableName = "Notifications")
public class AddFriendNotification implements Serializable{

    public static final String ID_FIELD_COLUMN_NAME = "ID";
    public static final String SOURCE_EMAIL_FIELD_COLUMN_NAME  = "SourceEmail";
    public static final String TARGET_USER_FIELD_COLUMN_NAME = "TargetUser_ID";
    public static final String MISSED_NOTIFICATION_COLUMN_NAME = "MissedNotification";

    @DatabaseField (generatedId = true, columnName = ID_FIELD_COLUMN_NAME)
    Integer id;
    @DatabaseField (columnName = SOURCE_EMAIL_FIELD_COLUMN_NAME)
    String sourceEmail;
    @DatabaseField (columnName = MISSED_NOTIFICATION_COLUMN_NAME, dataType = DataType.BOOLEAN)
    boolean missedNotification;
    @DatabaseField (foreign = true, foreignAutoRefresh = true, columnName = TARGET_USER_FIELD_COLUMN_NAME)
    User targetUser;


    public AddFriendNotification() {}

    public AddFriendNotification(String sourceEmail, User targetUser, Boolean missedNotification){
        this.sourceEmail = sourceEmail;
        this.targetUser = targetUser;
        this.missedNotification = missedNotification;
    }

    //GETTERS
    public String getSourceEmail() { return sourceEmail; }
    public User getTargetUser() { return targetUser; }
    public boolean isMissedNotification() { return missedNotification; }

    @Override
    public boolean equals(Object obj) {
        AddFriendNotification object1 = this;
        if(object1 == obj) return true;
        if(obj instanceof AddFriendNotification){
            AddFriendNotification object2 = (AddFriendNotification) obj;
            if(object1.hashCode() == object2.hashCode()) return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return sourceEmail.hashCode();
    }
}
