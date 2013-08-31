/*
 *    https://code.google.com/p/vellum - Contributed by Evan Summers
 * 
 */
package vellum.type;

import java.util.ArrayList;

/**
 *
 * @author evan.summers
 */
public class UniqueList<T> extends ArrayList<T> {
    
    @Override
    public boolean add(T element) {
        if (contains(element)) {
            throw new IllegalArgumentException(element.toString());
        }
        return super.add(element);
    }
    
}
