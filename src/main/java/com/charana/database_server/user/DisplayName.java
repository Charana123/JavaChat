package com.charana.database_server.user;

import java.io.Serializable;

public class DisplayName implements Serializable{
    String firstName, lastName;

    public DisplayName(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.join(" ", firstName, lastName);
    }
}
