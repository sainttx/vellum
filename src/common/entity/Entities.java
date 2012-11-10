/*
 * Apache Software License 2.0, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2012, Evan Summers
 * 
 */
package common.entity;

/**
 *
 * @author evan
 */
public class Entities {
 
    public static Comparable getId(Object object) {
        if (object == null) return null;
        if (object instanceof Entity) {
            Entity entity = (Entity) object;
            return entity.getId();
        }
        return object.hashCode();
    }
}
