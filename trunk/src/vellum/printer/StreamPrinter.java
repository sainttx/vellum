/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2011, iPay (Pty) Ltd, Evan Summers
 */

package vellum.printer;

import java.io.PrintStream;

/**
 *
 * @author evanx
 */
public interface StreamPrinter extends Printer {

    public PrintStream getPrintStream();

}
