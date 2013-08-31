/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
 * 
 */
package vellum.entity;

/**
 *
 * @author evan.summers
 */
public class Entities {
 
    public static Comparable getId(Object object) {
        if (object == null) return null;
        if (object instanceof IdEntity) {
            IdEntity entity = (IdEntity) object;
            return entity.getId();
        }
        return object.hashCode();
    }
    
    public static boolean equals(IdEntity entity, IdEntity other) {
        if (entity == null || other == null) return false;
        if (entity == other) return true;
        return entity.equals(other);
    }
}
