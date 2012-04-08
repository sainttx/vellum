/*
 * Copyright Evan Summers
 * 
 */
package venigma.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import venigma.common.AdminUser;

/**
 *
 * @author evan
 */
public class CipherStorage {
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

    public AdminUser getAdminUser(String username) {
        return userMap.get(username);
    }
}
