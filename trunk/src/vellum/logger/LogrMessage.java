/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */
package vellum.logger;

/**
 *
 * @author evans
 */
public class LogrMessage {
    LogrLevel level;
    Object[] args;
    
    public LogrMessage(LogrLevel level, Object[] args) {
        this.level = level;
        this.args = args;
    }
    
    public LogrLevel getLevel() {
        return level;
    }

    public Object[] getArgs() {
        return args;
    }
    
   
       
}
