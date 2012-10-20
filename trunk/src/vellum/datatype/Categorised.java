/*
 * 
 */
package vellum.datatype;

/**
 *
 * @author evans
 */
public interface Categorised<T> {
    public T getCategory();
    public void setCategory(T category);
}
