/*
 * Copyright Evan Summers
 * 
 */
package venigma.server.data;

import java.sql.Connection;
import java.sql.DriverManager;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;
import vellum.util.RowSets;
import venigma.common.AdminUser;
import venigma.common.AdminUserPair;
import venigma.common.KeyInfo;
import venigma.common.KeyInfoAdminUserPair;
import venigma.server.CipherConfig;
import venigma.server.CipherContext;
import venigma.server.CipherProperties;
import venigma.server.storage.*;

/**
 *
 * @author evan
 */
public class CipherStorage {

    Logr logger = LogrFactory.getLogger(CipherStorage.class);
    IdStorage<AdminUser> adminUserStorage = new IdStorage();
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
        initH2();
    }

    public void initH2() throws Exception {
        if (properties.databaseStorePassword != null) {
        }
        Class.forName(databaseConnectionInfo.getDriver());
        Connection connection = getConnection();
        new SchemaConnection(connection).verifySchema();
        releaseConnection(connection);
    }

    public Connection getConnection() throws Exception {
        return DriverManager.getConnection(databaseConnectionInfo.getUrl(),
                databaseConnectionInfo.getUsername(), properties.buildDatabasePassword());
    }    
    
    public void releaseConnection(Connection connection) {
        RowSets.close(connection);
    }

    public IdStorage<AdminUser> getAdminUserStorage() {
        return adminUserStorage;
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
}
