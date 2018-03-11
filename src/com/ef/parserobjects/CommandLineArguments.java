package com.ef.parserobjects;

import com.ef.exception.ParsingException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Command line argument processor for the Parser class.
 *
 * @author adelimon
 */
public class CommandLineArguments {

    /**
     * Store the arguments by name.
     */
    private Map<String, String> argsByName;

    private static final String ACCESS_LOG = "accesslog";
    private static final String START_DATE = "startdate";
    private static final String DURATION = "duration";
    private static final String THRESHOLD = "threshold";

    /**
     * Instantiate this class with arguments from the command line.
     * @param argsFromCommandLine command line arguments from a program.
     */
    public CommandLineArguments(String[] argsFromCommandLine, int minimumLength) throws ParsingException {
        argsByName = new HashMap<String, String>();
        if (argsFromCommandLine.length < minimumLength) {
            // error here
            // TODo: do this properly
            ParsingException ex = new ParsingException(
                    String.format("There should be %d arguments, %d were passed in", minimumLength, argsFromCommandLine.length)
            );
            throw ex;
        }
        for (String commandLineArg : argsFromCommandLine) {
            // get the argument value and split them by equal sign
            // hack off the trailing slashes as we don't need those
            commandLineArg = commandLineArg.replace("--", "");
            // split by the = sign to get the argument value
            String[] argSplit = commandLineArg.split("=");
            if (argSplit.length != 2) {
                // error here
                ParsingException ex = new ParsingException(
                        String.format("Arguments should be in the format a=b, %s was passed in.", commandLineArg)
                );

                throw ex;
            }
            argsByName.put(argSplit[0].toLowerCase(), argSplit[1]);
        }
    }

    /**
     * Get the access log path.
     * @return access log path
     */
    public String getAccessLog() {
        return argsByName.get(ACCESS_LOG);
    }

    public LocalDateTime getStartDate() {
        String startDate = argsByName.get(START_DATE);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
        return LocalDateTime.parse(startDate, formatter);
    }

    public String getDuration() {
        return argsByName.get(DURATION);
    }

    public int getThreshold() {
        return Integer.parseInt(argsByName.get(THRESHOLD));
    }
}
