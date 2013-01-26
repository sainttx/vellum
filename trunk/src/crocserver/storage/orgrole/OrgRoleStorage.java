/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.orgrole;

import crocserver.storage.adminuser.AdminUser;
import crocserver.storage.common.CrocStorage;
import crocserver.storage.org.Org;
import java.util.ArrayList;
import java.util.List;
import vellum.entity.LongIdEntityMapStorage;
import vellum.storage.StorageException;
import vellum.storage.StorageExceptionType;

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
    
    public Org getOrg(AdminUser user, String certName) throws StorageException {
        List<OrgRole> list = getOrgRoleList(user);
        if (list.isEmpty()) {
            throw new StorageException(StorageExceptionType.NOT_FOUND, user.getUserName(), certName);
        }
        return list.get(0).getOrg();
    }            
}
