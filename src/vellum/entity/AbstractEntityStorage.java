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
public abstract class AbstractEntityStorage<I, E> {
    
    public abstract E find(I id) throws SQLException ;
        
}
