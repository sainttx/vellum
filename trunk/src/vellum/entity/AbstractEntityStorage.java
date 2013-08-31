/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
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
