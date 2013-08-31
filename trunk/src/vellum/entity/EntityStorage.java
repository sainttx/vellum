/*
 * Contributed (2013) by Evan Summers via https://code.google.com/p/vellum
 * 
 */
package vellum.entity;

import java.sql.SQLException;

/**
 *
 * @author evan.summers
 */
public interface EntityStorage<I, E> {
    public I insert(E entity) throws SQLException;
    public void update(E entity) throws SQLException;
    public E find(I id) throws SQLException;
        
}
