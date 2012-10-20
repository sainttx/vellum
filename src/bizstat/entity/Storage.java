/*
 */
package bizstat.entity;

/**
 *
 * @author evans
 */
public interface Storage {
    public <E> E get(Class<E> entityType, Comparable id);
    public <E> E find(Class<E> entityType, Comparable id);
    public <E> E findNullable(Class<E> entityType, Comparable id);
    
}
