package com.charana.database_server.user;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "DBTextMessages")
public class DBTextMessage {

    public final static String TEXT_CONTENT_COLUMN_NAME = "TextContent";
    public final static String TIME_STAMP_COLUMN_NAME = "TimeStamp";
    public final static String SENT_BY_USER_COLUMN_NAME = "SentByUser";
    public final static String FRIEND_COLUMN_NAME = "Friend_ID";

    @DatabaseField (generatedId = true)
    Integer id;
    @DatabaseField (columnName = TIME_STAMP_COLUMN_NAME)
    String timeStamp;
    @DatabaseField (columnName = SENT_BY_USER_COLUMN_NAME)
    boolean sentByUser;
    @DatabaseField (columnName = TEXT_CONTENT_COLUMN_NAME)
    String textContent;
    @DatabaseField (foreign = true, foreignAutoRefresh = true, columnName = FRIEND_COLUMN_NAME)
    Friend friend;

    public DBTextMessage(){ }

    public DBTextMessage(String timeStamp, boolean sentByUser, String textContent, Friend friend) {
        this.timeStamp = timeStamp;
        this.sentByUser = sentByUser;
        this.textContent = textContent;
        this.friend = friend;
    }

    public String getTimeStamp() { return timeStamp; }

    public boolean isSentByUser() { return sentByUser; }

    public String getTextContent() { return textContent; }

    public Friend getFriend() { return friend; }
}
