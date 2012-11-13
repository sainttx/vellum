/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author evan
 */
public class EntityMap<E> {

    Class entityType;
    Map<Comparable, IdEntity> entityMap = new HashMap();
    
    public EntityMap(Class entityType) {
        this.entityType = entityType;
    }

    public Class getEntityType() {
        return entityType;
    }    
    
    public void put(IdEntity entity) {
        entityMap.put(entity.getId(), entity);
    }
    
    public E get(Comparable id) {
        return (E) entityMap.get(id);
    }

    public List<E> getExtentList() {
        return new ArrayList(entityMap.values());
    }

    public List<E> getList(Class<E> entityType, Matcher<E> matcher) {
        List<E> entityList = new ArrayList();
        for (E entity : getExtentList()) {
            if (matcher.matches(entity)) {
                entityList.add(entity);
            }
        }
        return entityList;
    }
    
    public List<IdEntity> getEntityList() {
        return new ArrayList(entityMap.values());
    }
    
}
