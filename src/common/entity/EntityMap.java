/*
 * Copyright Evan Summers
 * 
 */
package common.entity;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author evan
 */
public class EntityMap<E> {

    Class entityType;
    Map<Comparable, Entity> entityMap = new HashMap();
    
    public EntityMap(Class entityType) {
        this.entityType = entityType;
    }    
    
    public void put(Entity entity) {
        entityMap.put(entity.getId(), entity);
    }
    
    public E get(Comparable id) {
        return (E) entityMap.get(id);
    }
    
}
