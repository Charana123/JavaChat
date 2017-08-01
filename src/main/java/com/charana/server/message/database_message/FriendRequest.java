package com.charana.server.message.database_message;

import com.charana.database_server.user.User;

import java.io.Serializable;

public class FriendRequest implements Serializable{
    public final Account sourceAccount;
    public final boolean missedNotification;

    public FriendRequest(Account sourceAccount, boolean missedNotification){
        this.sourceAccount = sourceAccount;
        this.missedNotification = missedNotification;
    }
}
