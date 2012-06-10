/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package venigma.server;

import java.sql.SQLException;
import venigma.data.IdEntity;

/**
 *
 * @author evan
 */
public interface EntityConnection<I extends Comparable, E extends IdEntity> {
    public boolean exists(I id) throws SQLException;
    public E find(I id) throws SQLException;
    public void insert(E entity) throws SQLException;
    public void update(E entity) throws SQLException;
    public void delete(E entity) throws SQLException;        
}
