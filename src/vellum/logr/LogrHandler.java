/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */
package vellum.logr;

/**
 *
 * @author evans
 */
public interface LogrHandler {
   
    public void handle(LogrContext context, LogrRecord message);

}
