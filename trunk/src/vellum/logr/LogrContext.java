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
    LogrProvider provider;
    LogrLevel level;
    String sourceName;
    String name;
    
    public LogrContext(LogrProvider provider, LogrLevel level, Class source, String name) {
        this.provider = provider;
        this.level = level;
        this.sourceName = source.getSimpleName();
        this.name = name;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getName() {
        return name;
    }

    public LogrLevel getLevel() {
        return level;
    }
        
    @Override
    public String toString() {
        return name;
    }

    
}
