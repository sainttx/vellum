/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 * Supported by BizSwitch.net
 * (c) Copyright 2011, iPay (Pty) Ltd, Evan Summers
 */

package bizmon.printer;

import java.io.PrintStream;

/**
 *
 * @author evans
 */
public class PrinterFactory {

    public static Printer newPrinter(PrintStream stream) {
        return new PrintStreamAdapter(stream);
    }
}
