package com.ef.parserobjects;

public class BlockedIp {

    private String ipAddress;

    private int count;

    private int threshold;

    public BlockedIp(String ipAddress, int count, int threshold) {
        this.ipAddress = ipAddress;
        this.count = count;
        this.threshold = threshold;
    }

    public String getBlockedReason() {
        String blockedReason = String.format(
            "The IP address {0} was blocked because it made {1} requests which is over the threshold of {2}.",
                ipAddress, count, threshold
        );
        return blockedReason;
    }
}
