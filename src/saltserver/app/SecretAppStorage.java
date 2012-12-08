/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package saltserver.app;

import vellum.storage.DataSourceConfig;
import javax.sql.DataSource;
import saltserver.storage.schema.SaltSchema;
import saltserver.storage.secret.SecretValueStorage;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.storage.*;

/**
 *
 * @author evan
 */
public class SecretAppStorage {

    Logr logger = LogrFactory.getLogger(SecretAppStorage.class);
    ConnectionPool connectionPool;
    DataSource dataSource;
    
    public SecretAppStorage(DataSourceConfig dataSourceInfo) {
        this(new SimpleConnectionPool(dataSourceInfo));

    }
            
    public SecretAppStorage(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }
    
    public void init() throws Exception {        
        new SaltSchema(this).verifySchema();
        getSecretStorage().validate();
    }

    public SecretValueStorage getSecretStorage() {
        return new SecretValueStorage(this);
    }
       
    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }
}
