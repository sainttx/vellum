/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package venigmon.storage;

import bizstat.entity.StatusInfo;
import bizstat.server.BizstatServer;
import vellum.storage.DataSourceInfo;
import javax.sql.DataSource;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.storage.ConnectionPool;
import venigmon.storage.schema.SchemaPrinter;

/**
 *
 * @author evan
 */
public class VenigmonStorage {

    Logr logger = LogrFactory.getLogger(VenigmonStorage.class);
    BizstatServer server;
    DataSourceInfo dataSourceInfo;
    ConnectionPool connectionPool; 
    DataSource dataSource;

    public VenigmonStorage(BizstatServer server, ConnectionPool connectionPool) {
        this.server = server;
        this.connectionPool = connectionPool;
    }

    public void init() throws Exception {
        Class.forName(dataSourceInfo.getDriver());
        new SchemaStorage(this).verifySchema();
        new SchemaPrinter().handle(connectionPool, System.out, "PUBLIC");
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

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }
        
}
