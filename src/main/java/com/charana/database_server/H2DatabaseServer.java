package com.charana.database_server;

import com.charana.login_window.utilities.database.SQLiteDatabaseConnector;
import org.h2.tools.Server;

import java.net.URISyntaxException;
import java.sql.*;

public class H2DatabaseServer {

    public static void main(String[] args){
        try{
            Class.forName("org.h2.Driver");

            //Start server
            Server server = Server.createTcpServer("-tcpPort", "9081");
            server.start();

            //Create database server on localhost:9081
            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost:9081/~/database");

            //Create & Configure Database
            Statement stm = conn.createStatement();
            stm.execute("CREATE TABLE IF NOT EXISTS Accounts (Email TINYTEXT, Password TINYTEXT, AccountName TINYTEXT, Status TINYTEXT, Gender TINYTEXT, About MEDIUMTEXT, Birthday TINYTEXT)");
        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }
}
