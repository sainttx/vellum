/*
 * Apache Software License 2.0
 * Contributed (2013) by Evan Summers via https://code.google.com/p/vellum
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
