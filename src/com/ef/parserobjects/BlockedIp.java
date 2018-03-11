package com.ef.parserobjects;

/**
 * An IP Address that was blocked because it met the criteria specified by the user.
 *
 * @author adelimon
 */
public class BlockedIp {

    private String ipAddress;

    private int count;

    private int threshold;

    private static final String BLOCKED_MESSAGE =
            "The IP address %s was blocked because it made %s requests which is over the threshold of %s.";

    /**
     * Constructor for the BlockedIP class that takes in the IP, the count of requests, and the
     * threshold.
     * @param ipAddress
     * @param count
     * @param threshold
     */
    public BlockedIp(String ipAddress, int count, int threshold) {
        this.ipAddress = ipAddress;
        this.count = count;
        this.threshold = threshold;
    }

    /**
     * Why the IP was blocked.
     * @return
     */
    public String getBlockedReason() {
        return String.format(BLOCKED_MESSAGE, ipAddress, count, threshold);
    }

    /**
     * The IP address.
     * @return
     */
    public String getIpAddress() {
        return this.ipAddress;
    }
}
