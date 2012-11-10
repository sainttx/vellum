/*
 * Apache Software License 2.0, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2012, Evan Summers
 * 
 */
package vellum.storage;

import java.sql.SQLException;

/**
 *
 * @author evan
 */
public interface ConnectionPool {
    public ConnectionEntry takeEntry() throws SQLException;
    public void releaseConnection(ConnectionEntry connectionEntry) throws SQLException;
    
}
