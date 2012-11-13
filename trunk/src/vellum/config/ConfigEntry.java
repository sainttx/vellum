/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.config;

import vellum.type.ComparableTuple;

/**
 *
 * @author evan
 */
public class ConfigEntry {
    final String type;
    final String name;
    final ComparableTuple key;
    final PropertiesMap properties = new PropertiesMap();

    public ConfigEntry(String type, String name) {
        this.type = type;
        this.name = name;
        this.key = ComparableTuple.newInstance(type, name);
    }

    public ComparableTuple getKey() {
        return key;
    }
    
    public String getType() {
        return type;
    }
    
    public String getName() {
        return name;
    }

    public PropertiesMap getProperties() {
        return properties;
    }
    
    
}
