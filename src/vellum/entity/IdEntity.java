/*
 * Apache Software License 2.0, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2012, Evan Summers
 * 
 */
package vellum.entity;

/**
 *
 * @author evan
 */
public interface IdEntity<T extends Comparable> extends Comparable<IdEntity> {
    public T getId();
}
