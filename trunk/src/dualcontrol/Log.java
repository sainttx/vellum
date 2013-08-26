/*
 * Apache Software License 2.0, (c) Copyright 2013, Evan Summers
 * 
 */
package dualcontrol;

import java.util.Arrays;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class Log {

    public static void trace(Logger logger, Object ... args) {
        logger.trace(Arrays.toString(args));
    }

    public static void debug(Logger logger, Object ... args) {
        logger.debug(Arrays.toString(args));
    }
    
    public static void info(Logger logger, Object ... args) {
        logger.info(Arrays.toString(args));        
    }
    
    public static void warn(Logger logger, Object ... args) {
        logger.warn(Arrays.toString(args));
    }

    public static void error(Logger logger, Object ... args) {
        logger.error(Arrays.toString(args));
    }
    
}
