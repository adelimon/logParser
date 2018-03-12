package com.ef;

import com.ef.database.ConnectionPool;
import com.ef.database.LogQuery;
import com.ef.exception.ParsingException;
import com.ef.parserobjects.BlockedIp;
import com.ef.parserobjects.BlockedIpCollection;
import com.ef.parserobjects.CommandLineArguments;
import com.ef.parserobjects.LogFileLineCollection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Properties;

/**
 * A parser in Java that parses web server access log file, loads the log to MySQL and checks if a given IP makes more
 * than a certain number of requests for the given duration.
 *
 * Takes "startDate", "duration" and "threshold" as command line arguments.
 * "startDate" is of "yyyy-MM-dd.HH:mm:ss" format, "duration" can take only "hourly", "daily" as inputs and
 * "threshold" can be an integer.
 *
 * Uses the arguments provided to output the IP Addresses of the blocked IPs that made too many requests.  Also loads
 * the raw log file to the database, along with analytics about which IPs were blocked and why.
 *
 * @author adelimon
 * @author WalletHub recruiting (problem statement)
 */
public class Parser {

    private static void printProgress(String message) {
        System.out.println(LocalDateTime.now().toString() + " -- " + message);
    }

    public static void initDatabase() throws ClassNotFoundException, IOException {
        Properties dbConnectionProperties = new Properties();
        dbConnectionProperties.put("database.driver", "com.mysql.jdbc.Driver");
        dbConnectionProperties.put("database.url","jdbc:mysql://localhost:3306/log");
        dbConnectionProperties.put("database.user", "root");
        dbConnectionProperties.put("database.password", "");
        ConnectionPool.init(dbConnectionProperties);
    }

    public static void main(String[] args) {
        try {
            initDatabase();

            CommandLineArguments commandLineArguments = new CommandLineArguments(args, 4);

            String fileName = commandLineArguments.getAccessLog();
            printProgress("Reading log file...");
            LogFileLineCollection logfile = new LogFileLineCollection(fileName);
            printProgress("Read log file from disk. writing to DB. This may take a bit depending on how big your log is");
            logfile.persist();
            printProgress("Raw log file saved to DB.");

            LogQuery query = new LogQuery(commandLineArguments.getStartDate(), commandLineArguments.getDuration(),
                    commandLineArguments.getThreshold());

            // now run the query, get the results and save them to the DB
            BlockedIpCollection blockedIpCollection = new BlockedIpCollection(query, commandLineArguments.getThreshold());
            for (BlockedIp blockedIp : blockedIpCollection) {
                System.out.println(blockedIp.getIpAddress());
            }
            blockedIpCollection.persist();
        } catch (ParsingException ex) {
            System.err.println("An error has occurred, error message is " + ex.getMessage());
            ex.printStackTrace(System.err);
        } catch (IOException e) {
            System.err.println("an Error has occurred reading the file");
            e.printStackTrace(System.err);
        } catch (SQLException e) {
            System.err.println("an error occurred loading to the database");
            e.printStackTrace(System.err);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(System.err);
        }
    }
}
