package com.charana.login_window.utilities.database.user;

import java.io.Serializable;

public class Birthday implements Serializable{
    Integer day, year;
    Month month;

    public Birthday(Integer day, Month month, Integer year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    @Override
    public String toString() {
        return String.join("/", day.toString(), month.toString(), year.toString());
    }
}
