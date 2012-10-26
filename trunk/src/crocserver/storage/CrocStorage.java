/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage;

import vellum.datatype.EntityCache;
import bizstat.entity.ServiceRecord;
import vellum.storage.DataSourceConfig;
import javax.sql.DataSource;
import vellum.datatype.SimpleEntityCache;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.storage.*;
import crocserver.schema.SchemaPrinter;

/**
 *
 * @author evan
 */
public class CrocStorage {

    Logr logger = LogrFactory.getLogger(CrocStorage.class);
    DataSourceConfig dataSourceInfo;
    ConnectionPool connectionPool;
    DataSource dataSource;
    EntityCache<String> entityCache;

    public CrocStorage(DataSourceConfig dataSourceInfo) {
        this(new SimpleEntityCache(), new SimpleConnectionPool(dataSourceInfo));
    }
            
    public CrocStorage(EntityCache typeCache, ConnectionPool connectionPool) {
        this.entityCache = typeCache;
        this.connectionPool = connectionPool;
    }
    
    public void init() throws Exception {
        Class.forName(dataSourceInfo.getDriver());
        new CrocSchema(this).verifySchema();
        new SchemaPrinter().handle(connectionPool, System.out, "PUBLIC");
    }

    public AdminUserStorage getAdminUserStorage() {
        return new AdminUserStorage(this);
    }

    public ServiceRecordStorage getServiceRecordStorage() {
        return new ServiceRecordStorage(this);
    }

    public <T> T getEntity(Class<T> type, String name) {
        T value = entityCache.get(type, name);
        if (value == null) {
            throw new StorageException(StorageExceptionType.NOT_FOUND, name);
        }
        return value;
    }

    public void insert(ServiceRecord serviceRecord) {
        try {
            getServiceRecordStorage().insert(serviceRecord);
        } catch (Exception e) {
            logger.warn(e, "setStatusInfo", serviceRecord);
        }
    }

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }
}
