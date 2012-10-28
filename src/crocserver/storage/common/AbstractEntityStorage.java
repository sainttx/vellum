/*
 * Copyright Evan Summers
 * 
 */
package crocserver.storage.common;

import java.sql.SQLException;

/**
 *
 * @author evan
 */
public abstract class AbstractEntityStorage<I, E> {
    
    public abstract E find(I id) throws SQLException ;
        
}
