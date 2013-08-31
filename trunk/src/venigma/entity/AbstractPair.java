/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 * 
 */
package venigma.entity;

/**
 *
 * @author evan.summers
 */
public abstract class AbstractPair implements EntityPair {
    IdPair idPair;

    public AbstractPair() {
    }
    
    public AbstractPair(IdPair idPair) {
        this.idPair = idPair;
    }

    public void setIdPair(IdPair idPair) {
        this.idPair = idPair;
    }

    @Override
    public IdPair getIdPair() {
        return idPair;
    }
    
    @Override
    public int compareTo(EntityPair other) {
        return getIdPair().compareTo(other.getIdPair());
    }
    
    @Override
    public int hashCode() {
        return getIdPair().hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof EntityPair) {
            EntityPair other = (EntityPair) object;
            return getIdPair().equals(other.getIdPair());
        }
        return false;
    }
    
    @Override
    public String toString() {
        return getIdPair().toString();
    }
}
