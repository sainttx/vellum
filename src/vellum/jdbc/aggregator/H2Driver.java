/*
 * Copyright Evan Summers
 * 
 */
package vellum.jdbc.aggregator;

/**
 *
 * @author evan
 */
public class H2Driver extends VDriver {

    public H2Driver() {
        delegate = new org.h2.Driver();
    }    
    
}
