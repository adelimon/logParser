package com.ef.database;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;

/**
 * A query ran against the log table.
 */
public class LogQuery {

    private static final String DURATION_HOURLY = "hourly";
    private static final String DURATION_DAILY = "daily";

    private static final String HOURLY_QUERY = "select  " +
            "ip_address, count(ip_address) num_requests " +
            "from  " +
            "raw_log " +
            "where  " +
            "timestamp >= 'TIMESTAMP' and " +
            "timestamp < date_add('TIMESTAMP', interval 1 hour) " +
            "group by ip_address " +
            "having count(ip_address) >= THRESHOLD " +
            "order by num_requests desc";

    // this query will get the values for the duration of the day after the time we set.  What a handy thing to have
    // in a database's query language!
    private static final String DAILY_QUERY = "select  " +
            "ip_address, count(ip_address) num_requests " +
            "from  " +
            "raw_log " +
            "where  " +
            "timestamp >= 'TIMESTAMP' and " +
            "timestamp < date_add('TIMESTAMP', interval 1 day) " +
            "group by ip_address " +
            "having count(ip_address) >= THRESHOLD " +
            "order by num_requests desc";

    private String runnableQuery;
    
    public LogQuery(LocalDateTime startDate, String duration, int threshold) {
        switch (duration.toLowerCase()) {
            case DURATION_HOURLY:
                buildRunnableQuery(HOURLY_QUERY, startDate, threshold);
                break;
            case DURATION_DAILY:
                buildRunnableQuery(DAILY_QUERY, startDate, threshold);
                break;
        }
    }

    public String toSelectQueryString() {
        return runnableQuery;
    }

    private void buildRunnableQuery(String baseQuery, LocalDateTime startDate, int threshold) {
        // seeing this method of processing the query you may be thinking "but what about SQL injection?"
        // That is a valid concern.  Honestly I figured it was better to have a clean way of doing the query
        // than including prepared statements here.  also, this class requires that the datatype be passed in
        // I'm sure there is a way to do SQL injection with that but not that I could think of or test for.
        // also in a real world situation, I'd use an ORM such as Hibernate or JPA and avoid this type of
        // thing entirely!
        runnableQuery = baseQuery.replace("TIMESTAMP", startDate.toString());
        runnableQuery = runnableQuery.replace("THRESHOLD", threshold+"");
    }
}
