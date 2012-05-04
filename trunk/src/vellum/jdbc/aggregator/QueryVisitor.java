/*
 * Copyright Evan Summers
 * 
 */
package vellum.jdbc.aggregator;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author evan
 */
public interface QueryVisitor {
    public ResultSet executeQuery(PreparedStatementHandler handler, String sql) throws SQLException;
    
}
