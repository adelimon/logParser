package com.ef.parserobjects;

import com.ef.database.ConnectionPool;
import com.ef.database.LogQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A persistable collection of blocked IP Addresses.
 *
 * @author adelimon
 */
public class BlockedIpCollection implements PersistableCollection, Iterable<BlockedIp> {

    private List<BlockedIp> storage;

    /**
     * Create the collection from a query against the log.
     *
     * @param query Query object that is the basis of the collection.
     * @param threshold The number of requests taht will get you blocked.
     * @throws SQLException If there are any errors working with the DB because this collection accesses the DB to init.
     */
    public BlockedIpCollection(LogQuery query, int threshold) throws SQLException {
        storage = new ArrayList<BlockedIp>();
        Connection connection = ConnectionPool.getConnection();
        PreparedStatement selectQuery = connection.prepareStatement(query.toSelectQueryString());
        ResultSet rs = selectQuery.executeQuery(query.toSelectQueryString());
        while (rs.next()) {
            String ipAddress = rs.getString("ip_address");
            int count = rs.getInt("num_requests");
            add(ipAddress, count, threshold);
        }
    }

    private void add(String ipAddress, int count, int threshold) {
        BlockedIp blocked = new BlockedIp(ipAddress, count, threshold);
        storage.add(blocked);
    }

    /**
     * Save the collection to its corresponding database table.
     * @return the number of rows saved.
     * @throws SQLException
     */
    @Override
    public int persist() throws SQLException {
        int count = 0;
        ConnectionPool.cleanTable("blocked_ip_log");
        Connection connection = ConnectionPool.getConnection();
        String sql = "insert into blocked_ip_log (ip_address, block_reason) values (?, ?)";
        PreparedStatement insertStatement = connection.prepareStatement(sql);
        for (BlockedIp blockedIp : storage) {
            insertStatement.clearParameters();
            insertStatement.setString(1, blockedIp.getIpAddress());
            insertStatement.setString(2, blockedIp.getBlockedReason());
            insertStatement.addBatch();
            count++;
        }
        insertStatement.executeBatch();
        connection.commit();
        return count;
    }

    /**
     * Get the iterator for this collection.
     * @return iterator for this collection.
     */
    @Override
    public Iterator<BlockedIp> iterator() {
        // Just grab the list iterator because that's really all this class is, a wrapper for a list
        // Blocked IPs.
        return storage.iterator();
    }
}
