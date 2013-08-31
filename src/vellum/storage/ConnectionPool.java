/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
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
