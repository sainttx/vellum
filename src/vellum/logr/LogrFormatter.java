/*
 */
package vellum.logr;

/**
 *
 * @author evans
 */
public interface LogrFormatter {
    public String format(LogrContext context, LogrMessage message);
}
