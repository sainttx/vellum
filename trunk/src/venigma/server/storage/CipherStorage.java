/*
 * Copyright Evan Summers
 * 
 */
package venigma.server.storage;

import vellum.logger.Logr;
import vellum.logger.LogrFactory;
import venigma.common.AdminUser;

/**
 *
 * @author evan
 */
public class CipherStorage {
    Logr logger = LogrFactory.getLogger(CipherStorage.class);
    IdStorage<AdminUser> adminUserStorage = new IdStorage();
    IdStorage<KeyInfo> keyInfoStorage = new IdStorage();
    
    public CipherStorage() {
    }

    public IdStorage<AdminUser> getAdminUserStorage() {
        return adminUserStorage;
    }

    public IdStorage<KeyInfo> getKeyInfoStorage() {
        return keyInfoStorage;
    }                
}
