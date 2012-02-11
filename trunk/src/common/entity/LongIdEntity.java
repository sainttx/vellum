/*
 * Copyright Evan Summers
 * 
 */
package common.entity;

/**
 *
 * @author evan
 */
public class LongIdEntity extends AbstractEntity {
    protected Long id;
    
    @Override
    public Comparable getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
       
}
