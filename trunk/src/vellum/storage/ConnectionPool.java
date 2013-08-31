/*
 * Contributed (2013) by Evan Summers via https://code.google.com/p/vellum
 * 
 */
package vellum.storage;

import java.sql.SQLException;

/**
 *
 * @author evan.summers
 */
public interface ConnectionPool {
    public ConnectionEntry takeEntry() throws SQLException;
    public void releaseConnection(ConnectionEntry connectionEntry) throws SQLException;
    
}
