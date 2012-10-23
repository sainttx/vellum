/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package venigmon.storage;

import vellum.datatype.EntityCache;
import bizstat.entity.StatusInfo;
import vellum.storage.DataSourceInfo;
import javax.sql.DataSource;
import vellum.datatype.SimpleEntityCache;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.storage.*;
import venigmon.schema.SchemaPrinter;

/**
 *
 * @author evan
 */
public class VenigmonStorage {

    Logr logger = LogrFactory.getLogger(VenigmonStorage.class);
    DataSourceInfo dataSourceInfo;
    ConnectionPool connectionPool;
    DataSource dataSource;
    EntityCache<String> entityCache;

    public VenigmonStorage(DataSourceInfo dataSourceInfo) {
        this(new SimpleEntityCache(), new SimpleConnectionPool(dataSourceInfo));
    }
            
    public VenigmonStorage(EntityCache typeCache, ConnectionPool connectionPool) {
        this.entityCache = typeCache;
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

    public <T> T getEntity(Class<T> type, String name) {
        T value = entityCache.get(type, name);
        if (value == null) {
            throw new StorageException(StorageExceptionType.NOT_FOUND, name);
        }
        return value;
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
