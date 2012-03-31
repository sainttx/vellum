/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 * (c) Copyright 2011, iPay (Pty) Ltd, Evan Summers
 */

package vellum.logger;

import java.io.PrintStream;
import java.util.logging.Level;
import vellum.util.Args;

/**
 *
 * @author evanx
 */
public class Logr {
    Class source;
    PrintStream out = System.out;
    PrintStream err = System.err;
    Level level = Level.FINEST;
    String name;
    
    public Logr(Class source) {
        this.source = source;
        this.name = source.getSimpleName();
    }

    public Logr(Class source, String name) {
        this.source = source;
        this.name = source.getSimpleName() + "." + name;
    }
    
    public Logr(String name) {
        this.name = name;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
    
    public void info(Object ... args) {
        out.print(name);
        out.print(" ");        
        out.print("INFO");
        out.print(" ");        
        out.println(Args.format(args));
        flush();
    }

    public void trace(Object ... args) {
        if (level.intValue() > Level.FINER.intValue()) return;
        out.print(name);
        out.print(" ");        
        out.println(Args.format(args));
        flush();
    }

    private void flush() {
        System.out.flush();
        System.err.flush();
    }
    
    public void warn(Object ... args) {
        err.print(name);
        err.print(" ");        
        err.print("WARN ");        
        err.print(" ");        
        err.println(Args.format(args));
        Throwable throwable = getThrowable(args);
        if (throwable != null) {
            throwable.printStackTrace(err);
        }
    }
    
    protected Throwable getThrowable(Object[] args) {
        if (args.length > 0 && args[0] instanceof Throwable) {
            return (Throwable) args[0];
        }
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }                
}
