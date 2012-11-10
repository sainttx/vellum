/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2011, iPay (Pty) Ltd
 */
package vellum.log;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 *
 * @author evans
 */
public class LogAppenderProxy extends AppenderSkeleton {

    int count = 0;
    boolean debug = false;
    Appender appender = null;
    String targetClassName;
    Class targetClass;
    String jarFileName;
    File jarFile;
    long lastModified;

    public LogAppenderProxy() {
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    
    public void setTargetClassName(String targetClassName) {
        this.targetClassName = targetClassName;
    }

    public void setJarFileName(String jarFileName) {
        this.jarFileName = jarFileName;
        jarFile = new File(jarFileName);
    }

    protected void info(String message) {
        if (debug) {
            System.out.println(message);
        }

    }

    protected void warn(String message) {
        if (debug) {
            System.err.println(message);
        }  
    }

    protected void reload() {
        if (jarFile != null && jarFile.canRead() && jarFile.lastModified() != lastModified) {
            lastModified = jarFile.lastModified();
            appender = null;
            try {
                URL url = jarFile.toURI().toURL();
                URLClassLoader cl = new URLClassLoader(new URL[]{url}, getClass().getClassLoader());
                targetClass = cl.loadClass(targetClassName);
                appender = (Appender) targetClass.newInstance();
                if (debug) {
                    info("proxy appender reload " + appender + " " + targetClass.hashCode());
                }
            } catch (Exception e) {
                if (debug) {
                    e.printStackTrace(System.err);
                    warn("proxy appender " + e.getClass());
                }
            }
        }
    }

    @Override
    public void append(LoggingEvent le) {
        reload();
        if (appender != null) {
            appender.doAppend(le);
        } else {
            if (debug) {
                warn("proxy appender " + lastModified);
            }
        }
    }

    @Override
    public boolean requiresLayout() {
        if (appender != null) {
            return appender.requiresLayout();
        } else {
            return false;
        }
    }

    @Override
    public void close() {
    }

}
