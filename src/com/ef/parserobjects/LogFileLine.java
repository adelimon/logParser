package com.ef.parserobjects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * A single line in a log file.
 *
 * @author adelimon
 */
public class LogFileLine {

    private LocalDateTime timestamp;
    private String ipAddress;
    private String requestMethod;
    private int responseCode;
    private String userAgent;

    /**
     * Create a log file line from a raw log file line in a string.
     * @param rawData the raw log data.
     */
    public LogFileLine(String rawData) {
        String[] rawDataLine = rawData.split("\\|");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        timestamp = LocalDateTime.parse(rawDataLine[0], formatter);
        ipAddress = rawDataLine[1];
        requestMethod = rawDataLine[2];
        responseCode = Integer.parseInt(rawDataLine[3]);
        userAgent = rawDataLine[4];
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

}
