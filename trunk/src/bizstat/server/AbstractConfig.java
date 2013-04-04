/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package bizstat.server;

import vellum.config.PropertiesStringMap;

/**
 *
 * @author evan
 */
public class AbstractConfig {
    protected PropertiesStringMap properties;

    public AbstractConfig(PropertiesStringMap properties) {
        this.properties = properties;
    }
        
}
