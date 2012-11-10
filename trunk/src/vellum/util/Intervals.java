/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2011, iPay (Pty) Ltd, Evan Summers
 */

package vellum.util;

/**
 *
 * @author evanx
 */
public class Intervals {

    public static String formatDuration(long startMillis) {
        return String.format("%ds", (System.currentTimeMillis() - startMillis) / 1000);
    }
}
