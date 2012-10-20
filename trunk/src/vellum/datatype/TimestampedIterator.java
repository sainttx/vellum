/*
 */
package vellum.datatype;

import java.util.Iterator;

/**
 *
 * @author evans
 */
public class TimestampedIterator<T extends Timestamped> implements Iterator<T> {

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public T next() {
        throw new ArrayIndexOutOfBoundsException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
}
