/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */
package vellum.logger;

/**
 *
 * @author evans
 */
public class LogrContext {
    Class source;
    String name;
    LogrProvider provider;
    LogrLevel level;
    
    public LogrContext(LogrProvider provider, Class source, String name) {
        this.provider = provider;
        this.level = provider.getLevel();
        this.source = source;
        this.name = name;
    }

    public LogrLevel getLevel() {
        return level;
    }

    public Class getSource() {
        return source;
    }

    public String getName() {
        return name;
    }
 
}
