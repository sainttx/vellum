/*
 * Apache Software License 2.0
 * Supported by iPay (Pty) Ltd, BizSwitch.net
       Source https://code.google.com/p/vellum by @evanxsummers
 */

package vellum.printer;

import java.io.PrintStream;

/**
 *
 * @author evan.summers
 */
public interface StreamPrinter extends Printer {

    public PrintStream getPrintStream();

}
