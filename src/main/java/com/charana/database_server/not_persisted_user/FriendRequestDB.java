package com.charana.database_server.not_persisted_user;

import com.charana.database_server.user.User;

/**
 * Created by Charana on 8/1/17.
 */
public class FriendRequestDB {
    public final User sourceUser;
    public final boolean missedNotification;


    public FriendRequestDB(User sourceUser, boolean missedNotification) {
        this.sourceUser = sourceUser;
        this.missedNotification = missedNotification;
    }
}
