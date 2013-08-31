/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 * 
 */
package vellum.entity;

import java.sql.SQLException;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan.summers
 */
public abstract class AbstractEntityStorage<I, E> implements EntityStorage<I, E> {
    protected Logr logger = LogrFactory.getLogger(getClass());
    
    @Override
    public abstract E find(I id) throws SQLException;
        
}
