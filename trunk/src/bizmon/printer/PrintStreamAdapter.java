/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 * Supported by BizSwitch.net
 * (c) Copyright 2011, iPay (Pty) Ltd, Evan Summers
 */

package bizmon.printer;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 *
 * @author evans
 */
public class PrintStreamAdapter implements StreamPrinter {
    PrintStream printStream;

    public PrintStreamAdapter(PrintStream printStream) {
        this.printStream = printStream;
    }

    public PrintStreamAdapter(OutputStream outputStream) {
        printStream = new PrintStream(outputStream);
    }

    public PrintStream getPrintStream() {
        return printStream;
    }

    public void println() {
        printStream.println();
    }

    public void println(Object object) {
        printStream.println(object);
    }

    public void print(Object object) {
        printStream.print(object);
    }

    public void printf(String format, Object... args) {
        printStream.print(String.format(format, args));
    }

    public void printlnf(String format, Object... args) {
        printStream.println(String.format(format, args));
    }

    public void flush() {
        printStream.flush();
    }

    public void close() {
        printStream.flush();
        printStream.close();
    }

}
