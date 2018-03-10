package com.ef;

import com.ef.database.ConnectionPool;
import com.ef.database.LogQuery;
import com.ef.exception.ParsingException;
import com.ef.parserobjects.*;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class Parser {

    private static void exitWithError(String message) {
        System.out.println(message);
        System.exit(1);
    }

    private static void printProgress(String message) {
        System.out.println(LocalDateTime.now().toString() + " -- " + message);
    }

    public static void main(String[] args) {
        try {
            CommandLineArguments commandLineArguments = new CommandLineArguments(args, 4);

            String fileName = commandLineArguments.getAccessLog();
            printProgress("Reading log file...");
            LogFileLineCollection logfile = new LogFileLineCollection(fileName);
            printProgress("Read log file from disk. writing to DB. This may take a bit depending on how big your log is");
            logfile.persist();
            printProgress("Raw log file saved to DB.");

            LogQuery query = new LogQuery(commandLineArguments.getStartDate(), commandLineArguments.getDuration(),
                    commandLineArguments.getThreshold());
            printProgress(query.toSelectQueryString());

            // now run the query, get the results and save them to the DB
            BlockedIpCollection blockedIpCollection = new BlockedIpCollection();

            Connection connection = ConnectionPool.getConnection();
            PreparedStatement selectQuery = connection.prepareStatement(query.toSelectQueryString());
            ResultSet rs = selectQuery.executeQuery(query.toSelectQueryString());
            while (rs.next()) {
                String ipAddress = rs.getString("ip_address");
                int count = rs.getInt("num_requests");
                blockedIpCollection.add(ipAddress, count, commandLineArguments.getThreshold());
            }
        } catch (ParsingException ex) {
            System.err.println("An error has occurred, error message is " + ex.getMessage());
            ex.printStackTrace(System.err);
        } catch (IOException e) {
            System.err.println("an Error has occurred reading the file");
            e.printStackTrace(System.err);
        } catch (SQLException e) {
            System.err.println("an error occurred loading to the database");
            e.printStackTrace(System.err);
        }
    }
}
