/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2011, iPay (Pty) Ltd, Evan Summers
 */

package vellum.printer;

/**
 *
 * @author evanx
 */
public interface Printer {

    public void println();
    public void println(Object object);
    public void print(Object object);
    public void printf(String format, Object... args);
    public void printlnf(String format, Object... args);
    public void flush();
    public void close();

}
