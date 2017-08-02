package com.charana.database_server;

import com.charana.database_server.user.*;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.h2.tools.Server;

import java.sql.*;

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
            //TableUtils.dropTable(conn, Friend.class, false);
            TableUtils.createTableIfNotExists(conn, Friend.class);
            TableUtils.dropTable(conn, AddFriendNotification.class, true);
            TableUtils.createTableIfNotExists(conn, AddFriendNotification.class);


//            System.out.println();
//            Dao<Account, String> userDAO = DaoManager.createDao(conn, Account.class);
//            GenericRawResults<Account> userGenericRawResults = userDAO.queryRaw("SELECT * FROM Users WHERE firstName REGEXP '(?i)cha([a-z])*'", userDAO.getRawRowMapper());
//            List<Account> users = userGenericRawResults.getResults();
//            Account user = users.get(0);
//            System.out.println(user.getDisplayName());

        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }
}
