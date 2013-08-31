/*
 * Apache Software License 2.0
 * Supported by iPay (Pty) Ltd, BizSwitch.net
 * Licensed to the Apache Software Foundation (ASF) by Evan Summers
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
