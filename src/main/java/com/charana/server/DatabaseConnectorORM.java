package com.charana.server;


import com.charana.database_server.user.AddFriendNotificationDB;
import com.charana.database_server.user.DisplayName;
import com.charana.database_server.user.Friend;
import com.charana.database_server.user.User;
import com.j256.ormlite.dao.*;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;


public class DatabaseConnectorORM {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectorORM.class);
    public ConnectionSource conn;
    private Dao<User, String> userDAO;
    private Dao<Friend, Integer> friendDAO;
    private Dao<AddFriendNotificationDB, Integer> addFriendNotificationsDAO;

    DatabaseConnectorORM(ConnectionSource connection){ //TODO:: Throw error and log could not connect to database error from server
        try{
            this.conn = connection;
            userDAO = DaoManager.createDao(conn, User.class);
            friendDAO = DaoManager.createDao(conn, Friend.class);
            addFriendNotificationsDAO = DaoManager.createDao(conn, AddFriendNotificationDB.class);
        }
        catch (SQLException e){ e.printStackTrace(); }
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
            friendDAO.create(new Friend("albie@gmail.com", user));
            friendDAO.create(new Friend("rajat@gmail.com", user));
            addFriendNotificationsDAO.create(new AddFriendNotificationDB("albie@gmail.com", user));
            addFriendNotificationsDAO.create(new AddFriendNotificationDB("rajat@gmail.com", user));

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
//
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

    public User getAccount(String email){
        try{
            User user = userDAO.queryForId(email);
            if(user == null) {
                logger.info("No such user (email: {}) exists", email);
                return null;
            } else {
                logger.info("User Found (email: {}", email);
                return user;
            }
        } catch (SQLException e) {
            logger.error("Getting user (email: {}) failed", email, e);
            return null;
        }
    }

    public List<User> getFriends(String email){
        try{
            User user = userDAO.queryForId(email);
            CloseableIterator<Friend> friends = user.getFriends().closeableIterator();
            Where<User, String> query = userDAO.queryBuilder().where().idEq(friends.first().getEmail());
            friends.forEachRemaining(friend -> {
                try{ query.or().idEq(friend.getEmail()); }
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

    public List<User> getPossibleUsers(DisplayName displayName){
        logger.info("Finding possible contects for '{}'", displayName.toString());
        try {
            if (displayName.firstName != null && displayName.lastName == null) {
                String regexp = "(?i)" + displayName.firstName + "(\\w)*";
                HashSet<User> users1 = new HashSet<>(userDAO.queryRaw("SELECT * FROM Users WHERE firstName REGEXP ?", userDAO.getRawRowMapper(), regexp).getResults());
                HashSet<User> users2 = new HashSet<>(userDAO.queryRaw("SELECT * FROM Users WHERE lastName REGEXP ?", userDAO.getRawRowMapper(), regexp).getResults());
                users1.addAll(users2);
                return new ArrayList<>(users1);
            }
            else if(displayName.firstName != null && displayName.lastName != null){
                String regexp1 = "(?i)" + displayName.firstName + "(\\w)*";
                String regexp2 = "(?i)" + displayName.lastName + "(\\w)*";
                List<User> users = userDAO.queryRaw("SELECT * FROM Users WHERE firstName REGEX ? AND lastName REGEX ?", userDAO.getRawRowMapper(), regexp1, regexp2).getResults();
                return users;
            }
            else {
                logger.error("DisplayName empty");
                return null;
            }
        }
        catch (SQLException e){
            logger.error("Unable to find possible users", e);
            return null;
        }
    }

    public HashMap<AddFriendNotificationDB, User> getAddFriendNotifications(String email){
        try{
            User user = userDAO.queryForId(email);
            CloseableIterator<AddFriendNotificationDB> notifications = user.getAddFriendNotificationDB().closeableIterator();
            HashMap<AddFriendNotificationDB, User> notificationUserHashMap = new HashMap<>();
            notifications.forEachRemaining(addFriendNotification -> {
                try {
                    User sourceUser = userDAO.queryForId(addFriendNotification.getSourceEmail());
                    notificationUserHashMap.put(addFriendNotification, sourceUser);
                } catch (SQLException e){
                    logger.error("getAddFriendNotificationDB Error, Query for user by id {} failed", addFriendNotification.getSourceEmail(), e);
                }
            });
            return notificationUserHashMap;
        } catch (SQLException e){
            logger.error("getAddFriendNotificationDB Error, Query for user by id {} failed", email, e);
            return null;
        }
    }

    public boolean addFriendNotification(String sourceUserEmail, String targetUserEmail){
        try{
            User targetUser = userDAO.queryForId(targetUserEmail);
            ForeignCollection<AddFriendNotificationDB> addFriendNotificationDB = targetUser.getAddFriendNotificationDB();
            addFriendNotificationDB.add(new AddFriendNotificationDB(sourceUserEmail , targetUser));
            logger.info("Successfully added friend notification to targetUser {} from sourceUser {}", sourceUserEmail, targetUserEmail);
            return true;
        }
        catch (SQLException e){
            logger.error("Could add friend notification to targetUser {} from sourceUser {}", targetUserEmail, sourceUserEmail, e);
            return false;
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
            ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:h2:tcp://localhost:9081/~/Desktop/Application/database");
            DatabaseConnectorORM dbConnector = new DatabaseConnectorORM(connectionSource);
            //List<User> friends = dbConnector.getFriends("charananandasena@yahoo.com");
//            System.out.println(friends.size());
//            friends.forEach(friend -> System.out.println(friend.getEmail()));
            User user = dbConnector.getAccount("charananandasena@yahoo.com");
            System.out.println(user.getEmail());
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
