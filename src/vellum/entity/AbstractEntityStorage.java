/*
 * Copyright Evan Summers
 * 
 */
package vellum.entity;

import java.sql.SQLException;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan
 */
public abstract class AbstractEntityStorage<I, E> {
    protected Logr logger = LogrFactory.getLogger(getClass());
    
    public abstract E find(I id) throws SQLException ;
        
}
