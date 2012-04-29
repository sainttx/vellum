/*
 * Copyright Evan Summers
 * 
 */
package venigma.common;

/**
 *
 * @author evan
 */
public class AdminUserPair implements EntityPair {
    AdminUser adminUser;
    AdminUser otherAdminUser;
    char[] password;
    
    public AdminUserPair(AdminUser adminUser, AdminUser otherAdminUser) {
        this.adminUser = adminUser;
        this.otherAdminUser = otherAdminUser;
    }
        
    @Override
    public IdPair getIdPair() {
        return new IdPair(adminUser.getId(), otherAdminUser.getId());
    }

    public AdminUser getAdminUser() {
        return adminUser;
    }

    public AdminUser getOtherAdminUser() {
        return otherAdminUser;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public char[] getPassword() {
        return password;
    }        
}
