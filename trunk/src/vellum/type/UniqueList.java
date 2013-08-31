/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
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
