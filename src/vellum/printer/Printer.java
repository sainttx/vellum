/*
 * Apache Software License 2.0
 * Supported by iPay (Pty) Ltd, BizSwitch.net
       Source https://code.google.com/p/vellum by @evanxsummers
 */

package vellum.printer;

/**
 *
 * @author evan.summers
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
