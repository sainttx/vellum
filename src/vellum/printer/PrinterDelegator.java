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
public class PrinterDelegator implements Printer {
    protected Printer out;

    public PrinterDelegator(Printer out) {
        this.out = out;
    }
    
    @Override
    public void println() {
        out.println();
    }

    @Override
    public void println(Object object) {
        out.println(object);
    }

    @Override
    public void print(Object object) {
        out.print(object);
    }

    @Override
    public void printf(String format, Object... args) {
        out.print(String.format(format, args));
    }

    @Override
    public void printlnf(String format, Object... args) {
        out.println(String.format(format, args));
    }

    @Override
    public void flush() {
        out.flush();
    }

    @Override
    public void close() {
        out.close();
    }

}
