/*
 * Copyright Evan Summers
 * 
 */
package vellum.jdbc.interceptor;

import java.sql.Driver;

/**
 *
 * @author evan
 */
public class H2Driver extends VDriver {

    public H2Driver() {
        try {
            delegate = (Driver) Class.forName("org.h2.Driver").newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }    
    
}
