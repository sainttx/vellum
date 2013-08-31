/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 * 
 */
package vellum.entity;

/**
 *
 * @author evan.summers
 */
public interface IdEntity<I extends Comparable> {
    public I getId();
}
