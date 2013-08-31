/*
 * Contributed (2013) by Evan Summers via https://code.google.com/p/vellum
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
