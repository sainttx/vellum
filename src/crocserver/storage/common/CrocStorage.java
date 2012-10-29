/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.common;

import crocserver.storage.schema.CrocSchema;
import crocserver.storage.adminuser.UserStorage;
import crocserver.storage.servicerecord.ServiceRecordStorage;
import vellum.datatype.EntityCache;
import vellum.storage.DataSourceConfig;
import javax.sql.DataSource;
import vellum.datatype.SimpleEntityCache;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.storage.*;
import crocserver.storage.schema.SchemaPrinter;
import crocserver.storage.org.OrgStorage;
import crocserver.storage.servicecert.ClientCertStorage;

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

    public UserStorage getUserStorage() {
        return new UserStorage(this);
    }

    public OrgStorage getOrgStorage() {
        return new OrgStorage(this);
    }
       
    public ClientCertStorage getServiceCertStorage() {
        return new ClientCertStorage(this);
    }
    
    public ServiceRecordStorage getServiceRecordStorage() {
        return new ServiceRecordStorage(this);
    }

    public <T> T getEntity(Class<T> type, String name) {
        T value = entityCache.get(type, name);
        if (value == null) {
            throw new StorageRuntimeException(StorageExceptionType.NOT_FOUND, name);
        }
        return value;
    }

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }
}
