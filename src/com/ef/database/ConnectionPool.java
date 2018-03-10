package com.ef.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * A pool of database connections to access the database.
 *
 * @author adelimon
 */
public class ConnectionPool {

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        // typically at an application level this would be handled by a server configuration.  Since I don't have
        // that here I've chosen a basic implementation.  Normally this would be a lot fancier.
        if (connection == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            Properties connectionProps = new Properties();
            connectionProps.put("user", "root");
            connectionProps.put("password", "");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/log", connectionProps);
        }
        connection.setAutoCommit(false);
        return connection;
    }

    public static void cleanTable(String tableName) throws SQLException {
        getConnection().prepareStatement("truncate table " + tableName).executeUpdate();
    }
}
