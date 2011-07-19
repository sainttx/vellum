/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 * (c) Copyright 2011, iPay (Pty) Ltd, Evan Summers
 */

package bizmon.printer;

import java.io.PrintStream;

/**
 *
 * @author evanx
 */
public interface StreamPrinter extends Printer {

    public PrintStream getPrintStream();

}
