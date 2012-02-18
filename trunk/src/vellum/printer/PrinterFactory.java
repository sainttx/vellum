/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 * (c) Copyright 2011, iPay (Pty) Ltd, Evan Summers
 */

package vellum.printer;

import java.io.PrintStream;

/**
 *
 * @author evanx
 */
public class PrinterFactory {

    public static Printer newPrinter(PrintStream stream) {
        return new PrintStreamAdapter(stream);
    }
}
