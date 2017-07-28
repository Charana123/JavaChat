package com.charana.database_server.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Charana on 7/25/17.
 */
public class Friends implements Serializable{
    final List<String> friendEmails;

    public Friends(List<String> friendEmails){
        this.friendEmails = friendEmails;
    }

    public List<String> getFriendEmails() { //Defensive copying (of mutable attribute)
        return new ArrayList<>(friendEmails);
    }
}
