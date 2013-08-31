/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
 * 
 */
package vellum.jdbc.interceptor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author evan.summers
 */
public class PreparedStatementHandler {
    VConnection connection;
    PreparedStatement delegate;
    ArrayList<Object> parameterArray = new ArrayList();
    
    public PreparedStatementHandler(VConnection connection, PreparedStatement delegate) {
        this.connection = connection;
        this.delegate = delegate;
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        return delegate.executeQuery(sql);
    }

    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        parameterArray.set(parameterIndex, x);
        delegate.setTimestamp(parameterIndex, x);
        
    }

    public void setString(int parameterIndex, String x) throws SQLException {
        parameterArray.set(parameterIndex, x);
        delegate.setString(parameterIndex, x);
    }

    public void set(PreparedStatement statement) throws SQLException {
        for (int i = 1; i < parameterArray.size(); i++) {
            statement.setObject(i, parameterArray.get(i));
        }
    }
    
}
