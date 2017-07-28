package com.charana.login_window.utilities.database;

import com.charana.database_server.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.sql.*;

public class SQLiteDatabaseConnector {
    static Logger logger = LoggerFactory.getLogger(SQLiteDatabaseConnector.class);
    static Connection conn;

    static {
        databaseInit();
    }

    static private void databaseInit(){
        try {
            //Establish connection to database "database" at server localhost:9081
            Class.forName("org.h2.Driver"); //Load JDBC H2 Driver class
            String pathToDatabase = SQLiteDatabaseConnector.class.getResource("/").toURI().getPath();
            conn = DriverManager.getConnection("jdbc:h2:tcp://localhost:9081/" + pathToDatabase + "database");
        }
        catch(ClassNotFoundException | URISyntaxException | SQLException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    //TODO:: Send a "Login" Message over the network with `email` and `password` parameters
    //TODO:: Get a result from the network for the login request

    static public boolean login(String email, String password) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Accounts WHERE Email=?");
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                if(rs.getString("Password").equals(password)) { return true; }
                else { return false; }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    static public boolean accountExists(String email){
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Accounts WHERE Email=?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return (getRows(rs) > 0);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    static public boolean resetPassword(String email, String newPassword){
        try{
            PreparedStatement pstmt = conn.prepareStatement("UPDATE Accounts SET Password=? WHERE Email=?");
            pstmt.setString(1, newPassword);
            pstmt.setString(2, email);
            boolean updateSuccess = pstmt.executeUpdate() == 1;
            return updateSuccess;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    static public void createAccount(User user){
        try {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Accounts VALUES (?,?,?,?,?,?,?)");
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getDisplayName().toString());
            pstmt.setString(4, null); //Status
            pstmt.setString(5, user.getGender().name());
            pstmt.setString(6, null); //About
            pstmt.setString(7, user.getBirthday());
            pstmt.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

//    //SQLite only supports TYPE_FORWARD_ONLY and CONCUR_READ_ONLY cursors (manual scroll only allowed)
//    static private int getRows(ResultSet rs) throws SQLException {
//        int rows = 0;
//        while(rs.next()) { rows++; }
//        return rows;
//    }

    //Gets number of rows in ResultSet and leaves ResultSet unchanged
    static private int getRows(ResultSet rs) throws SQLException{
        rs.last();
        int rows = rs.getRow();
        rs.beforeFirst();
        return rows;
    }

    //Debugging only
    static private void printResultSet(ResultSet rs) throws SQLException {
        while(rs.next()){
            System.out.println(rs.getRow() + " " + rs.getString("Email"));
        }
        System.out.println("ResultSet END!");
    }

//    public static void main(String[] args){
//        User user = new User("charananandasena@yahoo.com", "NaruSaku123", "Charana", Status.ONLINE, Gender.MALE, "Life is Good", new Birthday(7 , 10, 1997));
//        try {
//            createAccount(user);
//            boolean exists = accountExists(user.email);
//            if(exists){ System.out.println("EXISTS"); }
//            else { System.out.println("DOESN'T EXIST"); }
////            boolean valid = login(user.email, user.password);
////            if(valid) { System.out.println("VALID"); }
////            else { System.out.println("NOT VALID"); }
//        }
//        catch(SQLException e) {
//            e.printStackTrace();
//            System.exit(1);
//        }
//    }
}