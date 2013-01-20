/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.orgperm;

import vellum.entity.IdEntity;

/**
 *
 * @author evan
 */
public class OrgPerm implements IdEntity<Long> {
    Long id;
    
    @Override
    public Long getId() {
        return id;
    }


}
