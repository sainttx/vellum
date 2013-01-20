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
public abstract class AbstractEntityStorage<I, E> implements EntityStorage<I, E> {
    protected Logr logger = LogrFactory.getLogger(getClass());
    
    @Override
    public abstract E find(I id) throws SQLException;
        
}
