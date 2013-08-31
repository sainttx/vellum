/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
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
