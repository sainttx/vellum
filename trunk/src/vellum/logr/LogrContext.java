/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */
package vellum.logr;

/**
 *
 * @author evans
 */
public class LogrContext {
    String sourceName;
    String name;
    LogrProvider provider;
    
    public LogrContext(LogrProvider provider, Class source, String name) {
        this.provider = provider;
        this.sourceName = source.getClass().getName();
        this.name = name;
    }

    public String getName() {
        return name;
    }
 
}
