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
public class ConfigParserException extends RuntimeException {

    public ConfigParserException(ConfigExceptionType exceptionType, String type, String name) {
        super(Args.format(exceptionType, type, name));
    }
    
    public ConfigParserException(ConfigExceptionType exceptionType, int lineCount, String type, String name, String line) {
        super(Args.format(exceptionType, lineCount, type, name, line));
    }
    
    public ConfigParserException(int lineCount, String type, String name, String line) {
        super(Args.format(lineCount, type, name, line));
    }
    

    
}
