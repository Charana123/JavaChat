package com.charana.database_server.not_persisted_user;


import com.charana.database_server.user.User;

public class UserData {
    public final User user;
    public final long missedNotifications;

    public UserData(User user, long missedNotifications){
        this.user = user;
        this.missedNotifications = missedNotifications;
    }
}
