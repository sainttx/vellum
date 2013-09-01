/*
       Source https://code.google.com/p/vellum by @evanxsummers
 * 
 */
package vellum.jdbc.interceptor;

import java.sql.Driver;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 *
 * @author evan.summers
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
