/*
 */
package vellum.logr.other;

/**
 *
 * @author evans
 */
public interface LogrFormatter {
    public String format(LogrContext context, LogrRecord message);
}
