/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.storage;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author evan
 */
public interface ConnectionPool {
    public Connection getConnection() throws SQLException;
    public void releaseConnection(Connection connection, boolean ok) throws SQLException;
    
}
