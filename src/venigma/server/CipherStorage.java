/*
 * Copyright Evan Summers
 * 
 */
package venigma.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;
import venigma.common.AdminUser;

/**
 *
 * @author evan
 */
public class CipherStorage {
    Logr logger = LogrFactory.getLogger(CipherStorage.class);
    List<AdminUser> userList = new ArrayList();
    Map<String, AdminUser> userMap = new HashMap();

    public CipherStorage() {
    }
    
    public void init(List<AdminUser> userList) {        
        userList.addAll(userList);
        for (AdminUser user : userList) {
            userMap.put(user.getUsername(), user);
        }
    }

    public boolean exists(String username) {
        return userMap.containsKey(username);
    }
    
    public AdminUser getAdminUser(String username) {
        return userMap.get(username);
    }

    public void update(AdminUser user) throws Exception {
        logger.info("update", user);
    }

    public void addAdminUser(AdminUser user) {
        logger.info("addAdminUser", user);
    }

}
