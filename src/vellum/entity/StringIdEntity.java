/*
 * Contributed (2013) by Evan Summers via https://code.google.com/p/vellum
 * 
 */
package vellum.entity;

/**
 *
 * @author evan.summers
 */
public class StringIdEntity extends AbstractIdEntity {
    protected String id;
    protected String label;
    
    @Override
    public Comparable getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
