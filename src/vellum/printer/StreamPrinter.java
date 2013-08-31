/*
 * Apache Software License 2.0
 * Supported by iPay (Pty) Ltd, BizSwitch.net
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
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
