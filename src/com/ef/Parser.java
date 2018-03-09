package com.ef;

import com.ef.exception.ParsingException;
import com.ef.parserobjects.CommandLineArguments;
import com.ef.parserobjects.LogFileLine;
import com.ef.parserobjects.LogFileLineCollection;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
            List<LogFileLine> logFileLines = new ArrayList<LogFileLine>();

            String fileName = commandLineArguments.getAccessLog();
            LogFileLineCollection logfile = new LogFileLineCollection();

            // one could also use a stream here but I find this to be much clearer and also simple enough to
            // understand so I did it this way.
            printProgress("Reading log file...");
            List<String> allLines = Files.readAllLines(Paths.get(fileName));
            for (String fileLine : allLines) {
                logfile.add(new LogFileLine(fileLine));
            }
            printProgress("Read log file from disk. now writing to DB. This may take a bit depending on how big your log is");
            logfile.persist();
            printProgress("Raw log file saved to DB.");
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
