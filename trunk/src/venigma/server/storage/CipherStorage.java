/*
 * Copyright Evan Summers
 * 
 */
package venigma.server.storage;

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
    
    public CipherStorage() {
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
