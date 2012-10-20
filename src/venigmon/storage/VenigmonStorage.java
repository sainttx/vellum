/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package venigmon.storage;

import bizstat1.entity.Host;
import bizstat.entity.StatusInfo;
import bizstat.server.BizstatServer;
import vellum.storage.DataSourceConfig;
import vellum.storage.StorageExceptionType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import vellum.query.RowSets;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.storage.ConnectionPool;
import vellum.storage.StorageRuntimeException;
import venigmon.storage.schema.SchemaPrinter;

/**
 *
 * @author evan
 */
public class VenigmonStorage implements ConnectionPool {

    Logr logger = LogrFactory.getLogger(VenigmonStorage.class);
    BizstatServer server;
    DataSourceConfig dataSourceInfo;
    DataSource dataSource;

    public VenigmonStorage(BizstatServer server, DataSourceConfig dataSourceInfo) {
        this.server = server;
        this.dataSourceInfo = dataSourceInfo;
    }

    public void init() throws Exception {
        Class.forName(dataSourceInfo.getDriver());
        new SchemaStorage(this).verifySchema();
        new SchemaPrinter().handle(this, System.out, "PUBLIC");
    }

    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(dataSourceInfo.getUrl(), dataSourceInfo.getUser(), dataSourceInfo.getPassword());
        } catch (SQLException e) {
            throw new StorageRuntimeException(StorageExceptionType.CONNECTION_ERROR, e);
        }
    }

    @Override
    public void releaseConnection(Connection connection) {
        RowSets.close(connection);
    }

    public AdminUserStorage getAdminUserStorage() {
        return new AdminUserStorage(this);
    }

    public StatusInfoStorage getStatusInfoStorage() {
        return new StatusInfoStorage(this);
    }
    
    public <T> T get(Class<T> type, String name) {
        return server.getConfigStorage().get(type, name);
    }
    
    public void insert(StatusInfo statusInfo) {
        try {
            getStatusInfoStorage().insert(statusInfo);
        } catch (Exception e) {
            logger.warn(e, "setStatusInfo", statusInfo);
        }        
    }
}
