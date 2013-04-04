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
    final PropertiesStringMap properties = new PropertiesStringMap();

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

    public PropertiesStringMap getProperties() {
        return properties;
    }
    
    
}
