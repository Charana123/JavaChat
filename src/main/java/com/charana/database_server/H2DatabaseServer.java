package com.charana.database_server;

import com.charana.database_server.user.*;
import com.j256.ormlite.dao.*;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.h2.tools.Server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URLDecoder;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class H2DatabaseServer {

    public static void main(String[] args){
        try{
            Class.forName("org.h2.Driver");

            //Start server
            Server server = Server.createTcpServer("-tcpPort", "9081");
            server.start();

//            //Create database server on localhost:9081
//            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost:9081/~/database");
//
//            //Create & Configure Database
//            Statement stm = conn.createStatement();
//            stm.execute("CREATE TABLE IF NOT EXISTS Accounts (Email TINYTEXT, Password TINYTEXT, AccountName TINYTEXT, Status TINYTEXT, Gender TINYTEXT, About MEDIUMTEXT, Birthday TINYTEXT)");
//            stm.execute("DROP TABLE Friends");
//            stm.execute("CREATE TABLE Friends (FrienderEmail TINYTEXT, FriendeeEmail TINYTEXT)");
//            stm.execute("INSERT INTO Friends VALUES ('charananandasena@yahoo.com','albie@gmail.com'),('charananandasena@yahoo.com', 'rajat@gmail.com'), ('charananandasena@yahoo.com','sachin@gmail.com'), ('charananandasena@yahoo.com', 'javafx@gmail.com')");


            //Create Users Tables
            ConnectionSource conn = new JdbcConnectionSource("jdbc:h2:tcp://localhost:9081/~/Desktop/Application/database");
            TableUtils.createTableIfNotExists(conn, User.class);
            TableUtils.createTableIfNotExists(conn, Friend.class);
            TableUtils.createTableIfNotExists(conn, AddFriendNotification.class);

//            System.out.println();
//            Dao<User, String> userDAO = DaoManager.createDao(conn, User.class);
//            GenericRawResults<User> userGenericRawResults = userDAO.queryRaw("SELECT * FROM Users WHERE firstName REGEXP '(?i)cha([a-z])*'", userDAO.getRawRowMapper());
//            List<User> users = userGenericRawResults.getResults();
//            User user = users.get(0);
//            System.out.println(user.getDisplayName());

        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }
}
