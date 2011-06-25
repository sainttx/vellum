/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 * (c) Copyright 2011, iPay (Pty) Ltd, Evan Summers
 */

package common.logger;

/**
 *
 * @author evanx
 */
public class LogrFactory {

    public static Logr getLogger(Class source) {
        return new Logr(source);
    }

    public static final Logr logger = new Logr("global");
}
