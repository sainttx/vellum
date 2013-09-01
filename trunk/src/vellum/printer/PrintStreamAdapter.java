/*
 * Apache Software License 2.0
 * Supported by iPay (Pty) Ltd, BizSwitch.net
       Source https://code.google.com/p/vellum by @evanxsummers
 */

package vellum.printer;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 *
 * @author evan.summers
 */
public class PrintStreamAdapter implements StreamPrinter {
    PrintStream out;

    public PrintStreamAdapter(PrintStream printStream) {
        this.out = printStream;
    }
    
    public PrintStreamAdapter(OutputStream outputStream) {
        out = new PrintStream(outputStream);
    }

    @Override
    public PrintStream getPrintStream() {
        return out;
    }

    @Override
    public void println() {
        out.println();
    }

    @Override
    public void println(Object object) {
        out.println(object);
    }

    public void print(Object object) {
        out.print(object);
    }

    public void printf(String format, Object... args) {
        out.print(String.format(format, args));
    }

    public void printlnf(String format, Object... args) {
        out.println(String.format(format, args));
    }

    public void flush() {
        out.flush();
    }

    public void close() {
        out.flush();
        out.close();
    }

}
