package com.charana.database_server.user;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@DatabaseTable (tableName = "Friends")
public class Friend {
    @DatabaseField (generatedId = true, columnName = "ID")
    private int id;
    @DatabaseField (columnName = EMAIL_FIELD_COLUMN_NAME)
    private String email;
    @DatabaseField (foreign = true, foreignAutoRefresh = true, columnName = USER_FIELD_COLUMN_NAME)
    private User user;

    public static final String EMAIL_FIELD_COLUMN_NAME  = "Email";
    public static final String USER_FIELD_COLUMN_NAME = "User_ID";

    public Friend(){}

    public Friend(String email, User user){
        this.email = email;
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public User getUser() {
        return user;
    }
}
