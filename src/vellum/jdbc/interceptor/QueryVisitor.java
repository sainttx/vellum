/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.jdbc.interceptor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author evan
 */
public interface QueryVisitor {
    public ResultSet executeQuery(PreparedStatementHandler handler, String sql) throws SQLException;
    
}
