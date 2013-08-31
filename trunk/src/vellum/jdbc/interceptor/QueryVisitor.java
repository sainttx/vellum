/*
 * Licensed to the Apache Software Foundation (ASF) by Evan Summers
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
