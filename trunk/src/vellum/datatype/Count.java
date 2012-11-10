/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2011, iPay (Pty) Ltd, Evan Summers
 */
package vellum.datatype;

/**
 *
 * @author evanx
 */
public class Count {
    public static final int G = 1024*1024*1024;
    public static final int M = 1024*1024;
    public static final int K = 1024;
    public static final int _16M = 16*1024*1024;

    public static String prettySize(long length) {
        if (length > 5*M) {
            return String.format("%dM", length/M);
        } else if (length > 5*G) {
            return String.format("%dG", length/G);
        }
        return String.format("%d bytes", length);
    }

}
