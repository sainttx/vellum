/*
 * Copyright Evan Summers
 * 
 */
package vellum.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author evan
 */
public class SimpleConnectionPool implements ConnectionPool {

    DataSourceInfo dataSourceInfo;
    Queue<Connection> connectionQueue = new LinkedList();
    int poolSize = 0;
    int taken = 0;

    public SimpleConnectionPool(DataSourceInfo dataSourceInfo) {
        if (dataSourceInfo.getPoolSize() != null) {
            this.poolSize = dataSourceInfo.getPoolSize();
        } 
        this.dataSourceInfo = dataSourceInfo;
    }

    @Override
    public synchronized Connection getConnection() {
        Connection connection = connectionQueue.poll();
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(
                        dataSourceInfo.getUrl(), dataSourceInfo.getUser(), dataSourceInfo.getPassword());
            } catch (SQLException e) {
                throw new StorageRuntimeException(StorageExceptionType.CONNECTION_ERROR, e);
            }
        }
        taken++;
        return connection;
    }

    @Override
    public synchronized void releaseConnection(Connection connection, boolean ok) {
        taken--;
        if (ok && connectionQueue.size() < poolSize) {
            if (connectionQueue.offer(connection)) {
                return;
            }
        }
        close(connection);
    }

    static void close(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new StorageRuntimeException(StorageExceptionType.CONNECTION_ERROR, e);
        }
    }
}
