/*
 * Apache Software License 2.0
 * Supported by iPay (Pty) Ltd, BizSwitch.net
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
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
