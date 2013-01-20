/*
 * Copyright Evan Summers
 * 
 */
package vellum.entity;

import java.sql.SQLException;

/**
 *
 * @author evan
 */
public interface EntityStorage<I, E> {
    public I insert(E entity) throws SQLException;
    public void update(E entity) throws SQLException;
    public E find(I id) throws SQLException;
        
}
