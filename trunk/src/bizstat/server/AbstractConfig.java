/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package bizstat.server;

import vellum.config.PropertiesMap;

/**
 *
 * @author evan
 */
public class AbstractConfig {
    protected PropertiesMap properties;

    public AbstractConfig(PropertiesMap properties) {
        this.properties = properties;
    }
        
}
