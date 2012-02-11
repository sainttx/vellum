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
public class EntityManager<E> {
    Map<Class, EntityMap> typeMap = new HashMap();

    public EntityMap getMap(Class type) {
        EntityMap map = typeMap.get(type);
        if (map == null) {
            map = new EntityMap(type);
            typeMap.put(type, map);
        }
        return map;
    }
    
}
