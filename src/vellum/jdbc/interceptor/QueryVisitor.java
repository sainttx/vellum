/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
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
