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
    Class source;
    String name;
    LogrProvider provider;
    
    public LogrContext(LogrProvider provider, Class source, String name) {
        this.provider = provider;
        this.source = source;
        this.name = name;
    }

    public Class getSource() {
        return source;
    }

    public String getName() {
        return name;
    }
 
}
