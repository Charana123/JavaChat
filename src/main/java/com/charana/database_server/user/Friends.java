package com.charana.database_server.user;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Charana on 7/25/17.
 */
public class Friends implements Serializable{
    List<String> friendEmails;

    public Friends(){}

    public static Friends getInstance(List<String> friendEmails){
        Friends friends = new Friends();
        friends.setFriendEmails(friendEmails);
        return friends;
    }

    public List<String> getFriendEmails() {
        return friendEmails;
    }

    public void setFriendEmails(List<String> friendEmails) {
        this.friendEmails = friendEmails;
    }
}
