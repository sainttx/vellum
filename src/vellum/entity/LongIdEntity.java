/*
 * Contributed (2013) by Evan Summers via https://code.google.com/p/vellum
 * 
 */
package vellum.entity;

/**
 *
 * @author evan.summers
 */
public interface LongIdEntity extends IdEntity<Long> {
    public void setId(Long id);
       
}
