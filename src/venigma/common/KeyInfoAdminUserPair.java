/*
 * Copyright Evan Summers
 * 
 */
package venigma.common;

/**
 *
 * @author evan
 */
public class KeyInfoAdminUserPair implements EntityPair {
    KeyInfo keyInfo;
    AdminUser adminUser;
    
    public KeyInfoAdminUserPair(KeyInfo keyInfo, AdminUser adminUser) {
        this.keyInfo = keyInfo;
        this.adminUser = adminUser;
    }
        
    @Override
    public IdPair getIdPair() {
        return new IdPair(keyInfo.getId(), adminUser.getId());
    }

    public KeyInfo getKeyInfo() {
        return keyInfo;
    }

    public AdminUser getAdminUser() {
        return adminUser;
    }    
}
