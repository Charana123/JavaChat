package com.charana.server;


import com.charana.database_server.not_persisted_user.FriendRequestDB;
import com.charana.database_server.not_persisted_user.UserData;
import com.charana.database_server.user.*;
import com.charana.server.message.database_message.DisplayName;
import com.j256.ormlite.dao.*;
import com.j256.ormlite.stmt.*;
import com.j256.ormlite.support.ConnectionSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class DatabaseConnectorORM {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectorORM.class);
    public ConnectionSource conn;
    private Dao<User, String> userDAO;
    private Dao<Friend, Integer> friendDAO;
    private Dao<AddFriendNotification, Integer> addFriendNotificationsDAO;

    DatabaseConnectorORM(ConnectionSource connection){ //TODO:: Throw error and log could not connect to database error from server
        try{
            this.conn = connection;
            userDAO = DaoManager.createDao(conn, User.class);
            friendDAO = DaoManager.createDao(conn, Friend.class);
            addFriendNotificationsDAO = DaoManager.createDao(conn, AddFriendNotification.class);
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

    public boolean createUser(User user){
        try {
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
//            List<Account> users = userDAO.queryRaw(query, userDAO.getRawRowMapper(), email).getResults();
//
////            ResultSet rs = pstmt.executeQuery();
////            printResultSet(rs);
//        }
//        catch (SQLException e){
//            e.printStackTrace();
//        }
//        return true;
//    }

    public UserData getAccount(String email){
        try{
            User user = userDAO.queryForId(email);

            QueryBuilder<AddFriendNotification, Integer> queryBuilder = addFriendNotificationsDAO.queryBuilder();
            queryBuilder.where().eq(AddFriendNotification.TARGET_USER_FIELD_COLUMN_NAME, email).and().eq(AddFriendNotification.MISSED_NOTIFICATION_COLUMN_NAME, true);
            long missedNotificationsNum = queryBuilder.countOf();

            if(user == null) {
                logger.info("No such user (email: {}) exists", email);
                return null;
            } else {
                logger.info("Account Found (email: {}", email);
                return new UserData(user, missedNotificationsNum); //I need missed notifications number from somewhere
            }
        } catch (SQLException e) {
            logger.error("Getting user (email: {}) failed", email, e);
            return null;
        }
    }

    public List<User> getFriends(String email){
        try{
            User user = userDAO.queryForId(email);
            List<Friend> friends = new ArrayList<>(user.getFriends());
            if(!friends.isEmpty()){
                Where<User, String> query = userDAO.queryBuilder().where().idEq(friends.get(0).getEmail());
                friends.subList(1, friends.size()).forEach(friend -> {
                    try{ query.or().idEq(friend.getEmail()); }
                    catch (SQLException e) { e.printStackTrace(); }
                });
                return userDAO.query(query.prepare());
            }
            return new ArrayList<>();
        }
        catch (SQLException e){
            logger.error("Failed to get friends (closed connection)", e);
            return null;
        }
    }

    //TODO:: Filter all current friends from contact search results
    public List<User> getPossibleUsers(String userEmail, DisplayName displayName){
        logger.info("Finding possible contects for '{}'", displayName.toString());
        try {
            if (displayName.firstName != null && displayName.lastName == null) {
                String regexp = "(?i)" + displayName.firstName + "(\\w)*";
                HashSet<User> users1 = new HashSet<>(userDAO.queryRaw("SELECT * FROM Users WHERE firstName REGEXP ?", userDAO.getRawRowMapper(), regexp).getResults());
                HashSet<User> users2 = new HashSet<>(userDAO.queryRaw("SELECT * FROM Users WHERE lastName REGEXP ?", userDAO.getRawRowMapper(), regexp).getResults());
                users1.addAll(users2);
                users1.remove(new User(userEmail));
                return new ArrayList<>(users1);
            }
            else if(displayName.firstName != null && displayName.lastName != null){
                String regexp1 = "(?i)" + displayName.firstName + "(\\w)*";
                String regexp2 = "(?i)" + displayName.lastName + "(\\w)*";
                List<User> users = userDAO.queryRaw("SELECT * FROM Users WHERE (firstName REGEXP ?) AND (lastName REGEXP ?)", userDAO.getRawRowMapper(), regexp1, regexp2).getResults();
                users.remove(new User(userEmail));
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

    public List<FriendRequestDB> getAddFriendNotifications(String email){
        try{
            User user = userDAO.queryForId(email);

            //Get all AddFriendNotifications
            CloseableWrappedIterable<AddFriendNotification> notificationsIterator = user.getAddFriendNotification().getWrappedIterable();
            List<AddFriendNotification> AddFiendNotifications = StreamSupport.stream(notificationsIterator.spliterator(), false).collect(Collectors.toList());
            notificationsIterator.close();

            //Set all notification to seen (not missed)
            UpdateBuilder<AddFriendNotification, Integer> updateBuilder = addFriendNotificationsDAO.updateBuilder();
            updateBuilder.updateColumnValue(AddFriendNotification.MISSED_NOTIFICATION_COLUMN_NAME, false);
            updateBuilder.where().eq(AddFriendNotification.TARGET_USER_FIELD_COLUMN_NAME, email).and().eq(AddFriendNotification.MISSED_NOTIFICATION_COLUMN_NAME, true);
            updateBuilder.update();

            //Convert to FriendRequestDB and return
            List<FriendRequestDB> FriendNotifications = AddFiendNotifications.stream().map(addFriendNotificationDB -> {
                try {
                    User sourceUser = userDAO.queryForId(addFriendNotificationDB.getSourceEmail());
                    FriendRequestDB friendRequestDB = new FriendRequestDB(sourceUser, addFriendNotificationDB.isMissedNotification());
                    return Optional.of(friendRequestDB);
                }
                catch (SQLException e){
                    //TODO:: Remove friend notification from targetUser (if the sourceUser cannot be found)
                    //TODO:: Fix removeFriendNotification to take aurguments in the correct order
                    logger.error("getAddFriendNotification Error, Query for user by id {} failed", addFriendNotificationDB.getSourceEmail(), e);
                    return Optional.empty();
                }
            }).filter(Optional::isPresent).map(optionalFriendRequestDB -> (FriendRequestDB) optionalFriendRequestDB.get()).collect(Collectors.toList());
            Collections.reverse(FriendNotifications);
            return  FriendNotifications;

        } catch (SQLException | IOException e){
            logger.error("Querying notifications for user {} failed", email, e);
            return null;
        }
    }

    public boolean addFriendNotification(String sourceUserEmail, String targetUserEmail){
        try{
            User targetUser = userDAO.queryForId(targetUserEmail);
            ForeignCollection<AddFriendNotification> addFriendNotifications = targetUser.getAddFriendNotification();

            Boolean notificationPreviousSent = addFriendNotifications.stream()
                    .anyMatch(addFriendNotification -> addFriendNotification.getSourceEmail().equals(sourceUserEmail));
            if(!notificationPreviousSent){
                addFriendNotifications.add(new AddFriendNotification(sourceUserEmail , targetUser, true));
                logger.info("Successfully added friend notification to targetUser {} from sourceUser {}", sourceUserEmail, targetUserEmail);
                return true;
            }
            else {
                logger.info("Duplicate friend notification sent to targetUser {} from sourceUser {}", sourceUserEmail, targetUserEmail);
                return false;
            }
        }
        catch (SQLException e){
            logger.error("Could add friend notification to targetUser {} from sourceUser {}", targetUserEmail, sourceUserEmail, e);
            return false;
        }
    }

    public boolean removeFriendNotification(String sourceUserEmail, String targetUserEmail){
        try{
            DeleteBuilder<AddFriendNotification, Integer> deleteBuilder = addFriendNotificationsDAO.deleteBuilder();
            deleteBuilder.where().eq(AddFriendNotification.SOURCE_EMAIL_FIELD_COLUMN_NAME, targetUserEmail).and().eq(AddFriendNotification.TARGET_USER_FIELD_COLUMN_NAME, sourceUserEmail);
            PreparedDelete<AddFriendNotification> preparedDelete = deleteBuilder.prepare();
            addFriendNotificationsDAO.delete(preparedDelete);
            logger.info("Successfully Removed friend notification from sourceUserEmail {} to targetUserEmail {}", targetUserEmail, sourceUserEmail);
            return true;
        }
        catch (SQLException e){
            logger.error("Error Removed friend notification from sourceUserEmail {} to targetUserEmail {}", targetUserEmail, sourceUserEmail, e);
            return false;
        }
    }

    public boolean addFriend(String sourceUserEmail, String targetUserEmail){
        try{
            User sourceUser = userDAO.queryForId(sourceUserEmail);
            sourceUser.getFriends().add(new Friend(targetUserEmail, sourceUser));
            logger.info("Successfully added user {} to user {} friendlist", targetUserEmail, sourceUserEmail);
            return true;
        }
        catch (SQLException e){
            logger.error("Failed to add user {} to user {} friendlist", targetUserEmail, sourceUserEmail, e);
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
}
