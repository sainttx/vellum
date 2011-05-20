/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 * Supported by BizSwitch.net
 * (c) Copyright 2011, iPay (Pty) Ltd, Evan Summers
 */

package bizmon.printer;

/**
 *
 * @author evans
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
