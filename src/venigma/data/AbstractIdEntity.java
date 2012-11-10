/*
 * Apache Software License 2.0, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2012, Evan Summers
 * 
 */
package venigma.data;

/**
 *
 * @author evan
 */
public abstract class AbstractIdEntity implements IdEntity, Comparable<IdEntity> {

    public AbstractIdEntity() {
    }
    
    @Override
    public int compareTo(IdEntity other) {
        return getId().compareTo(other.getId());
    }
    
    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof IdEntity) {
            IdEntity other = (IdEntity) object;
            return getId().equals(other.getId());
        }
        return false;
    }
    
    @Override
    public String toString() {
        return getId().toString();
    }
}
