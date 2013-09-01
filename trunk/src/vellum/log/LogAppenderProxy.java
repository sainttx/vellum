/*
       Source https://code.google.com/p/vellum by @evanxsummers

       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements. See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.  
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
 * @author evan.summers
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
