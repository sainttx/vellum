/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
 * 
 */
package vellum.entity;

/**
 *
 * @author evan.summers
 */
public abstract class AbstractLongIdEntity extends AbstractIdEntity {
    protected Long id;
    
    @Override
    public Comparable getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
       
}
