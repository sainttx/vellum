/*
 * Apache Software License 2.0, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2012, Evan Summers
 * 
 */
package vellum.config;

import vellum.util.Args;

/**
 *
 * @author evan
 */
public class ConfigException extends RuntimeException {

    public ConfigException(ConfigExceptionType exceptionType) {
        super(Args.format(exceptionType));
    }
    
    public ConfigException(ConfigExceptionType exceptionType, String type, String name) {
        super(Args.format(exceptionType, type, name));
    }

    public ConfigException(ConfigExceptionType exceptionType, String propertyName) {
        super(Args.format(exceptionType, propertyName));
    }

    
}
