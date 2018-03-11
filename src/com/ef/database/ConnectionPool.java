package com.ef.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * A pool of database connections to access the database.
 *
 * @author adelimon
 */
public class ConnectionPool {

    private static int POOL_SIZE = 1;

    private static Connection connection;

    // This is here mostly to demonstrate the concept of using a pool of connections.  Normally this would be
    // handled at the server configuration level, such as in an app server or at the spring boot/tomcat level in a
    // microservice.
    private static List<Connection> poolStorage = new ArrayList<Connection>(0);
    private static String jdbcUrl;
    private static String userName;
    private static String password;


    public static void init(Properties connectionProperties) throws ClassNotFoundException {
        Class.forName(connectionProperties.getProperty("database.driver"));
        jdbcUrl = connectionProperties.getProperty("database.url");
        userName = connectionProperties.getProperty("database.user");
        password = connectionProperties.getProperty("database.password");
    }

    /**
     * Get a connection the database.  Note that this connection does not auto commit, so when you need to commit
     * (as in the case of a write operation) you need to call .commit on this object.
     *
     * @return Connection from the pool
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        // typically at an application level this would be handled by a server configuration.  Since I don't have
        // that here I've chosen a basic implementation.  Normally this would be a lot fancier.
        if (poolStorage.size() < POOL_SIZE) {
            Properties connectionProps = new Properties();
            connectionProps.put("user", userName);
            connectionProps.put("password", password);
            connection = DriverManager.getConnection(jdbcUrl, connectionProps);
            connection.setAutoCommit(false);
            poolStorage.add(connection);
        }
        return poolStorage.get(0);
    }

    /**
     * Clean up a table in the databse.
     * @param tableName The table name to clean.
     * @throws SQLException if there is an error doing this, such as a permissions error.
     */
    public static void cleanTable(String tableName) throws SQLException {
        Connection connection = getConnection();
        connection.prepareStatement("truncate table " + tableName).executeUpdate();
        connection.commit();
    }
}
