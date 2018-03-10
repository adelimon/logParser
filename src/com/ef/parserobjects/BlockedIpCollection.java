package com.ef.parserobjects;

import com.ef.database.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BlockedIpCollection implements PersistableCollection {

    private List<BlockedIp> storage;


    public BlockedIpCollection() {
        storage = new ArrayList<BlockedIp>();
    }

    public void add(String ipAddress, int count, int threshold) {
        BlockedIp blocked = new BlockedIp(ipAddress, count, threshold);
        storage.add(blocked);
    }

    @Override
    public int persist() throws SQLException {
        ConnectionPool.cleanTable("blocked_ip_log");
        Connection connection = ConnectionPool.getConnection();

        return 0;
    }
}
