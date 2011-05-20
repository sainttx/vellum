/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 * (c) Copyright 2011, iPay (Pty) Ltd, Evan Summers
 */

package bizmon.util;

/**
 *
 * @author evans
 */
public class Intervals {

    public static String formatDuration(long startMillis) {
        return String.format("%ds", (System.currentTimeMillis() - startMillis) / 1000);        
    }
}
