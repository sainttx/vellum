/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package saltserver.app;

import vellum.storage.DataSourceConfig;
import javax.sql.DataSource;
import saltserver.storage.schema.SaltSchema;
import saltserver.storage.secret.SecretStorage;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.storage.*;

/**
 *
 * @author evan
 */
public class SaltStorage {

    Logr logger = LogrFactory.getLogger(SaltStorage.class);
    ConnectionPool connectionPool;
    DataSource dataSource;
    
    public SaltStorage(DataSourceConfig dataSourceInfo) {
        this(new SimpleConnectionPool(dataSourceInfo));

    }
            
    public SaltStorage(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }
    
    public void init() throws Exception {        
        new SaltSchema(this).verifySchema();
        getSecretStorage().validate();
    }

    public SecretStorage getSecretStorage() {
        return new SecretStorage(this);
    }
       
    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }
}
