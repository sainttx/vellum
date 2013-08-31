/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
 * 
 */
package vellum.config;

import vellum.util.Args;

/**
 *
 * @author evan.summers
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
