/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
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
