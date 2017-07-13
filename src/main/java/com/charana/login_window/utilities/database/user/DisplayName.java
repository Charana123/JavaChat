package com.charana.login_window.utilities.database.user;

/**
 * Created by Charana on 7/13/17.
 */
public class DisplayName {
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
