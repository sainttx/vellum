/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
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
