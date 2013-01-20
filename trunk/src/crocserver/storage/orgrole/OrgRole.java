/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.orgrole;

import crocserver.storage.adminuser.AdminUser;
import crocserver.storage.adminuser.AdminUserRole;
import crocserver.storage.org.Org;
import vellum.entity.IdEntity;

/**
 *
 * @author evan
 */
public class OrgRole implements IdEntity<Long> {
    Long id;
    AdminUser user; 
    Org org;
    AdminUserRole role;
    
    @Override
    public Long getId() {
        return id;
    }


}
