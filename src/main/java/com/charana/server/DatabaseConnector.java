package com.charana.server;

import com.charana.database_server.user.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
            if(rs.next()){ //`if` instead of `while` because we only except 1 row in the resultset
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
            pstmt.setString(3, user.getDisplayName().toString());
            pstmt.setString(4, null); //Status
            pstmt.setString(5, user.getGender().name());
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

    public boolean getAccount(String email){
        //TODO:: Query & Convert ResultSet into User Object to return
        try{
            QueryRunner queryRunner = new QueryRunner();
            ResultSetHandler<List<User>> h = new BeanListHandler<>(User.class);
            String query = "SELECT Accounts.* FROM Accounts INNER JOIN Friends ON Accounts.Email = Friends.FriendeeEmail WHERE Friends.FrienderEmail = ?";
            List<User> friends = queryRunner.query(conn, query, h, email);
            friends.forEach(user -> System.out.println(user));

//            String query = "SELECT Accounts.* FROM Accounts INNER JOIN Friends ON Accounts.Email = Friends.FriendeeEmail WHERE Friends.FrienderEmail = ?";
//            PreparedStatement pstmt = conn.prepareStatement(query);
//            pstmt.setString(1,email);
//            ResultSet rs = pstmt.executeQuery();
//            printResultSet(rs);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return true;
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
    static private void printResultSet(ResultSet rs){ //TODO:: Function is a Functional Interface (make example)
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            List<String> columnNames = IntStream.range(1, metaData.getColumnCount() + 1).mapToObj(columnNumber -> {
                try { return metaData.getColumnName(columnNumber); }
                catch (SQLException e) { return "null"; }
            }).collect(Collectors.toList());
            while(rs.next()){
                for (String columnName : columnNames) {
                    System.out.println(columnName + "= " + rs.getString(columnName));
                }
                System.out.println("-----");
            }
            System.out.println("ResultSet END!");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        try{
            Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost:9081/~/Desktop/Application/database");
            DatabaseConnector dbConnector = new DatabaseConnector(connection);
            dbConnector.getAccount("charananandasena@yahoo.com");
        }
        catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }
}
