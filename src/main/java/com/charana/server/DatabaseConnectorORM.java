package com.charana.server;


import com.charana.database_server.user.Friends;
import com.charana.database_server.user.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.sun.org.apache.bcel.internal.generic.DALOAD;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class DatabaseConnectorORM {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectorORM.class);
    public ConnectionSource conn;
    Dao<User, String> userDAO;

    DatabaseConnectorORM(ConnectionSource connection){
        try{
            this.conn = connection;
            userDAO = DaoManager.createDao(conn, User.class);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean login(String email, String password){
        try{
            User user = userDAO.queryForId(email);
            if(user != null){
                if(user.getPassword().equals(password)){
                    logger.info("Successfully logged in");
                    return true;
                }
                else{
                    logger.info("Failed to login (wrong password)");
                    return false;
                }
            } else { //Should not happen because during login_email screen the existence of the email was checked
                logger.warn("Failed to login (no such account exists) ");
                return false;
            }
        }
        catch (SQLException e){
            logger.error("Failed to login (closed connection)", e);
            return false;
        }
    }

    public boolean accountExists(String email){
        try {
            User user = userDAO.queryForId(email);
            if(user != null){
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
            int updates = userDAO.updateRaw("UPDATE Users SET Password=? WHERE Email=?", newPassword, email);
            if(updates == 1){  //1 row was successfully updated
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
            //Debugging
            List<String> friends = new ArrayList<>();
            friends.addAll(Arrays.asList("rajat@gmail.com", "albie@gmail.com"));
            user.setFriends(Friends.getInstance(friends));

            int creates = userDAO.create(user);
            if(creates == 1){
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

//    public boolean getAccount(String email){
//        //TODO:: Query & Convert ResultSet into User Object to return
//        try{
//            String query = "SELECT Users.* FROM Accounts INNER JOIN FriendshipAssociations ON Users.Email = FriendshipAssociations.FriendeeEmail WHERE FriendshipAssociations.FrienderEmail = ?";
//            List<User> users = userDAO.queryRaw(query, userDAO.getRawRowMapper(), email).getResults();
//
////            ResultSet rs = pstmt.executeQuery();
////            printResultSet(rs);
//        }
//        catch (SQLException e){
//            e.printStackTrace();
//        }
//        return true;
//    }

    public List<User> getFriends(String email){
        try{
            User user = userDAO.queryForId(email);
            List<String> friendEmails = user.getFriends().getFriendEmails();
            Where<User, String> query = userDAO.queryBuilder().where().idEq(friendEmails.get(0));
            friendEmails.forEach(friendEmail -> {
                try{ query.or().idEq(friendEmail); }
                catch (SQLException e) { e.printStackTrace(); }
            });
            List<User> friendUserObjects = userDAO.query(query.prepare());
            return friendUserObjects;
        }
        catch (SQLException e){
            logger.error("Failed to get friends (closed connection)", e);
            return null;
        }
    }

    //Debugging Only
    private void printRawResults(GenericRawResults<String[]> rawResults){
        try{
            String[] columnNames = rawResults.getColumnNames();
            for (String[] row : rawResults.getResults()) {
                for (int column = 0; column < row.length; column++) {
                    System.out.println(columnNames[column] + " - " + row[column]);
                }
            }
            System.out.println("---------");
        }catch (SQLException e){ e.printStackTrace(); }
    }

    public static void main(String[] args){
        try{
            ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:h2:tcp://localhost:9081/~/database");
            DatabaseConnectorORM dbConnector = new DatabaseConnectorORM(connectionSource);
            List<User> friends = dbConnector.getFriends("charananandasena@yahoo.com");
            System.out.println(friends.size());
            friends.forEach(friend -> System.out.println(friend));
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
