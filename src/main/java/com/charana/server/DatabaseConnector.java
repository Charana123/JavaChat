package com.charana.server;

import com.charana.login_window.utilities.database.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnector.class);
    public final Connection conn;

    DatabaseConnector(Connection connection){
        this.conn = connection;
    }

    public boolean login(String email, String password) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Accounts WHERE Email=?");
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){ //`if` instead of `while` because we only except 1 recor
                if(rs.getString("Password").equals(password)) {
                    logger.info("Successfully logged in");
                    return true;
                } else {
                    logger.info("Failed to login (wrong password)");
                    return false;
                }
            } else { //Should not happen because during login_email screen the existence of the email was checked
                logger.warn("Failed to login (no such account exists) ");
                return false;
            }
        }
        catch (SQLException e){
            logger.error("Failed to login", e);
            return false;
        }
    }

    public boolean accountExists(String email){
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Accounts WHERE Email=?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            int rows = getRows(rs);
            if(rows == 1){ //If there is 1 row associated with the given email, the account exists
                logger.info("email: {} already exists", email);
                return true;
            }
            else { return false; }
        }
        catch (SQLException e){
            logger.error("Failed to check if account exists (closed connection)", e);
            return false;
        }
    }

    public boolean resetPassword(String email, String newPassword){
        try{
            PreparedStatement pstmt = conn.prepareStatement("UPDATE Accounts SET Password=? WHERE Email=?");
            pstmt.setString(1, newPassword);
            pstmt.setString(2, email);
            if(pstmt.executeUpdate() == 1){  //1 row was successfully updated
                logger.info("Successfully reset password");
                return true;
            }
            else { return false; }
        }
        catch (SQLException e){
            logger.error("Failed to reset password (closed connection)", e);
            return false;
        }

    }

    public boolean createAccount(User user){
        try {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Accounts VALUES (?,?,?,?,?,?,?)");
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getDisplayName());
            pstmt.setString(4, null); //Status
            pstmt.setString(5, user.getGender());
            pstmt.setString(6, null); //About
            pstmt.setString(7, user.getBirthday());
            if(pstmt.executeUpdate() == 1){ //1 row was successfully created
                logger.info("Successfully created account");
                return true;
            }
            else { throw new SQLException(); }
        }
        catch (SQLException e){
            logger.error("Failed to create account (closed connection)", e);
            return false;
        }
    }

//    //SQLite only supports TYPE_FORWARD_ONLY and CONCUR_READ_ONLY cursors
//    static private int getRows(ResultSet rs) throws SQLException {
//        int rows = 0;
//        while(rs.next()) { rows++; }
//        return rows;
//    }

    //Gets number of rows in ResultSet and leaves ResultSet unchanged
    private int getRows(ResultSet rs) throws SQLException{
        rs.last();
        int rows = rs.getRow();
        rs.beforeFirst();
        return rows;
    }

    //Debugging only
    private void printResultSet(ResultSet rs) throws SQLException {
        while(rs.next()){
            System.out.println(rs.getRow() + " " + rs.getString("Email"));
        }
        System.out.println("ResultSet END!");
    }
}
