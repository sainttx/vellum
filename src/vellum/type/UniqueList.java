/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
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
