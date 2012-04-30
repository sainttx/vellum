/*
 * Copyright Evan Summers
 * 
 */
package venigma.server.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;
import vellum.util.RowSets;
import venigma.common.AdminUserPair;
import venigma.common.KeyInfo;
import venigma.common.KeyInfoAdminUserPair;
import venigma.server.CipherConfig;
import venigma.server.CipherContext;
import venigma.server.CipherProperties;
import venigma.server.storage.DatabaseConnectionInfo;
import venigma.server.storage.IdStorage;
import venigma.server.storage.PairStorage;
import venigma.server.storage.StorageExceptionType;
import venigma.server.storage.StorageRuntimeException;

/**
 *
 * @author evan
 */
public class CipherStorage {

    Logr logger = LogrFactory.getLogger(CipherStorage.class);
    IdStorage<KeyInfo> keyInfoStorage = new IdStorage();
    PairStorage<KeyInfoAdminUserPair> KeyInfoAdminUserPairStorage = new PairStorage();
    PairStorage<AdminUserPair> adminUserPairStorage = new PairStorage();
    CipherContext context;
    CipherConfig config;
    CipherProperties properties;
    DatabaseConnectionInfo databaseConnectionInfo;
    
    public CipherStorage(CipherContext context) {
        this.context = context;
    }

    public void init() throws Exception {
        config = context.getConfig();
        databaseConnectionInfo = config.databaseConnectionInfo;
        properties = context.getProperties();
        Class.forName(config.databaseConnectionInfo.getDriver());
        if (properties.databaseStorePassword != null) {
            initEncryptedDatabase();
        }
        Connection connection = getConnection();
        new SchemaConnection(connection).verifySchema();
        releaseConnection(connection);
    }

    private void initEncryptedDatabase() throws Exception {
    }
    
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(databaseConnectionInfo.getUrl(),
                    databaseConnectionInfo.getUsername(), properties.buildDatabasePassword());
        } catch (SQLException e) {
            throw new StorageRuntimeException(StorageExceptionType.CONNECTION_ERROR, e);
        }
    }    

    public void releaseConnection(Connection connection) {
        RowSets.close(connection);
    }

    public IdStorage<KeyInfo> getKeyInfoStorage() {
        return keyInfoStorage;
    }

    public PairStorage<KeyInfoAdminUserPair> getKeyInfoAdminUserPairStorage() {
        return KeyInfoAdminUserPairStorage;
    }

    public PairStorage<AdminUserPair> getAdminUserPairStorage() {
        return adminUserPairStorage;
    }

    public AdminUserConnection getAdminUserConnection() {
        return new AdminUserConnection(getConnection());
    }
}
