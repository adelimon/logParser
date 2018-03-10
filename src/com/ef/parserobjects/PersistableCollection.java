package com.ef.parserobjects;

import java.sql.SQLException;

public interface PersistableCollection {

    public int persist() throws SQLException;

}
