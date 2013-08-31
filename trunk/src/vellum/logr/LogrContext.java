/*
 * Apache Software License 2.0
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 */
package vellum.logr;

/**
 *
 * @author evan.summers
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
