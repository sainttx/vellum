/*
 * Licensed to the Apache Software Foundation (ASF) by Evan Summers
 * 
 */
package vellum.entity;

/**
 *
 * @author evan.summers
 */
public interface Matcher<E> {
    public boolean matches(E e);
    
}
