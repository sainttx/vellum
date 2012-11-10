/*
 * Apache Software License 2.0, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2012, Evan Summers
 * 
 */
package vellum.type;

import java.util.ArrayList;

/**
 *
 * @author evan
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
