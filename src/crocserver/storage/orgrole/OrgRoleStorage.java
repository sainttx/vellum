/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.orgrole;

import crocserver.storage.adminuser.AdminUser;
import crocserver.storage.common.CrocStorage;
import java.util.ArrayList;
import java.util.List;
import vellum.entity.LongIdEntityMapStorage;

/**
 *
 * @author evan
 */
public class OrgRoleStorage extends LongIdEntityMapStorage<Long, OrgRole> {

    CrocStorage storage;

    public OrgRoleStorage(CrocStorage storage) {
        this.storage = storage;
    }

    public List<OrgRole> getOrgRoleList(AdminUser user) {
        List<OrgRole> list = new ArrayList();
        for (OrgRole entity : super.getExtentList()) {
            if (entity.getUser().equals(user)) {
                list.add(entity);
            }
        }
        return list;
    }
    
}
