package com.charana.server;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

//Refer to Collections.synchronizedList() for details
public class DatabaseConnectionPool {
    Logger logger = LoggerFactory.getLogger(DatabaseConnectionPool.class);
    int databaseServerPort;
    InetAddress databaseServerIP;
    Queue<ConnectionSource> connectionPool = new LinkedList<>();
    AtomicInteger maxsize = new AtomicInteger(0);
    boolean moreConnections;

    /**
     * Tries to create a pool of `prefConnections` to a database server
     * If `prefConnections` is higher than the number of concurrent connections supported to the database say maxConnections
     * Then a pool of `maxConnections` is created instead with the side effect that no further getConnection() calls will
     * call createConnection() and instead block until the pool is non-empty
     * @param databaseServerIP ip of database server
     * @param databaseServerPort port of database server
     * @param prefConnections preferred number of connections to the database
     */
    public DatabaseConnectionPool(InetAddress databaseServerIP, int databaseServerPort, int prefConnections){
        this.databaseServerIP = databaseServerIP;
        this.databaseServerPort = databaseServerPort;

        moreConnections = true;
        try{
            Class.forName("org.h2.Driver");
            for(int totalConnections = 0; totalConnections < prefConnections; totalConnections++){
                try {
                    ConnectionSource conn = createConnection();
                    connectionPool.add(conn);
                }
                catch (SQLException e) { break; }
            }
        }
        catch (ClassNotFoundException e){ logger.error("class org.h2.Driver.class does not exist", e); }
    }

    /**
     *  returns a connections from the pool if there exist any
     *  If none exist and a connection can be made to the database server a new connection is created and returned
     *  If none exist and a connection to the database server cannot be made, the thread is sent to sleep to wait until the pool is non-empty
     * @return a valid connection to the database server
     * @throws InterruptedException
     */
    //All operations that are public (that can be accesses by multiple threads)  and change the state of the mutable object
    //must synchronize so as to avoid inconsistency issues
    public synchronized ConnectionSource getConnection() throws InterruptedException{
        if(connectionPool.size() == 0) {
            if(moreConnections) {
                try { return createConnection(); }
                catch (SQLException e) { while(connectionPool.size() == 0) { this.wait(); } }
            }
            else { while(connectionPool.size() == 0) { this.wait(); }  }
        }
        return connectionPool.poll();
    }


    /**
     * Releases a connection back into the pool
     * @param connection The connection to be released
     */
    public synchronized void closeConnection(ConnectionSource connection){
        connectionPool.offer(connection);
        this.notify();
    }

    //public methods that do not change the state of the mutable object can be accessed by multiple threads
    public int getCurrentConnections(){
        return connectionPool.size();
    }

    public int getMaxSize(){
        return maxsize.get();
    }

    /**
     * @return Returns a valid connection
     * @throws SQLException if connection could not be made to the database server (max connections reached) at which point a flag
     * is set such that any futher getConnection() requests avoid createConnection() calls and instead wait for the queue to become
     * non-empty
     */
    private ConnectionSource createConnection() throws SQLException {
        try {
            //Connection conn = DriverManager.getConnection("jdbc:h2:tcp://" + databaseServerIP.getHostName() + ":" + databaseServerPort + "/~/database");
            ConnectionSource conn = new JdbcConnectionSource("jdbc:h2:tcp://" + databaseServerIP.getHostName() + ":" + databaseServerPort + "/~/Desktop/Application/database");
            maxsize.incrementAndGet();
            return conn;
        }
        catch (SQLException e){
            logger.error("No more connections to database", e);
            moreConnections = false;
            throw e;
        }
    }

}
























