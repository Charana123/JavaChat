package com.charana.database_server.user;

import java.io.Serializable;

public final class Birthday implements Serializable{
    public final Integer day, year;
    public final Month month;

    public Birthday(Integer day, Month month, Integer year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    @Override
    public String toString() {
        return String.join("/", day.toString(), month.name(), year.toString());
    }

    public static Birthday fromString(String birthday){
        String[] dates = birthday.split("/");
        Integer day = Integer.parseInt(dates[0]);
        Month month = Month.valueOf(dates[1]);
        Integer year = Integer.parseInt(dates[2]);
        return new Birthday(day, month, year);
    }
}
