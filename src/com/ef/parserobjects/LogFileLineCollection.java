package com.ef.parserobjects;

import com.ef.database.ConnectionPool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A collection of lines in a log file that is searchable, sortable, and generally made to be flexible.
 *
 * @author adelimon
 */
public class LogFileLineCollection {

    private Collection<LogFileLine> storage;

    public LogFileLineCollection(String fileName) throws IOException {
        storage = new ArrayList<LogFileLine>();
        List<String> allLines = Files.readAllLines(Paths.get(fileName));
        for (String fileLine : allLines) {
            this.add(new LogFileLine(fileLine));
        }
    }

    public void add(LogFileLine line) {
        storage.add(line);
    }

    /**
     * Persist the collection to the database so that we can query it later on.
     * @return
     * @throws SQLException
     */
    public int persist() throws SQLException {
        int count = 0;
        // note: this approach uses a batch load method and raw JDBC.  I chose this because I wasn't sure if an external
        // library like JPA or Hibernate was allowed.
        // I Initially did this with a prepared statement for each query but that took too long, hence the switch
        // to batch insert.
        Connection databaseConnection = ConnectionPool.getConnection();
        // truncate the raw log in prepration for reloading it.  You could also create a new log table here or set
        // a flag in another table. This was just the simplest way to solve the problem.
        databaseConnection.prepareStatement("truncate table raw_log").executeUpdate();
        databaseConnection.setAutoCommit(false);
        String sql = "insert into raw_log (timestamp, ip_address, request_method, response_code, user_agent) "+
                "values (?, ?, ?, ?, ?)";
        PreparedStatement insertStatement = databaseConnection.prepareStatement(sql);
        for (LogFileLine line : storage) {
            insertStatement.clearParameters();
            insertStatement.setTimestamp(1, Timestamp.valueOf(line.getTimestamp()));
            insertStatement.setString(2, line.getIpAddress());
            insertStatement.setString(3, line.getRequestMethod());
            insertStatement.setInt(4, line.getResponseCode());
            insertStatement.setString(5, line.getUserAgent());
            insertStatement.addBatch();
        }
        int[] results = insertStatement.executeBatch();
        databaseConnection.commit();
        return count;
    }
}
