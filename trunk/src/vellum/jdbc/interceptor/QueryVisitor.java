/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
 * 
 */
package vellum.jdbc.interceptor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author evan.summers
 */
public interface QueryVisitor {
    public ResultSet executeQuery(PreparedStatementHandler handler, String sql) throws SQLException;
    
}
