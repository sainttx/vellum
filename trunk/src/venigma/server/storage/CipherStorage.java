/*
 * Copyright Evan Summers
 * 
 */
package venigma.server.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import venigma.common.KeyInfo;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;
import venigma.common.AdminUser;
import venigma.common.AdminUserPair;
import venigma.common.KeyInfoAdminUserPair;

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
    Connection connection; 
    
    public CipherStorage() {
    }

    public void init(char[] storePassword) throws Exception {
        initH2(storePassword);
    }

    public void initH2(char[] storePassword) throws Exception {
        DatabaseConnectionInfo databaseConnectionInfo = new DatabaseConnectionInfo();
        databaseConnectionInfo.setDriver("org.h2.Driver");
        databaseConnectionInfo.setUrl(String.format("jdbc:h2:~/%s;CIPHER=AES", databaseConnectionInfo.getName()));
        databaseConnectionInfo.setUsername("sa");
        databaseConnectionInfo.setPassword(String.format("%s %s", databaseConnectionInfo.getStorePassword(), 
                databaseConnectionInfo.getStorePassword()));
        connect(databaseConnectionInfo);
    }

    public void connect(DatabaseConnectionInfo databaseConnectionInfo) throws Exception {
        Class.forName(databaseConnectionInfo.getDriver());
        connection = DriverManager.getConnection(databaseConnectionInfo.getUrl(),
                databaseConnectionInfo.getUsername(), databaseConnectionInfo.getPassword());
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
