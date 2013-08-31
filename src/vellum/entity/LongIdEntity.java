/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
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
