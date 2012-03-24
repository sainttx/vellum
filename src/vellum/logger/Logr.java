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
    PrintStream out = System.err;
    Level level = Level.INFO;
    String name;
    
    public Logr(Class source) {
        this.source = source;
        this.name = source.getSimpleName();
    }

    public Logr(String name) {
        this.name = name;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
    
    public void info(Object ... args) {
        out.println(Args.format(args));
        out.flush();
    }

    public void trace(Object ... args) {
        if (level.intValue() > Level.FINER.intValue()) return;
        out.println(Args.format(args));
    }

    public void warn(Object ... args) {
        out.print("WARN ");
        out.println(Args.format(args));
        Throwable throwable = getThrowable(args);
        if (throwable != null) {
            throwable.printStackTrace(System.err);
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
        
}
