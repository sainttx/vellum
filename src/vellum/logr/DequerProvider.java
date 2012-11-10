/*
 * Apache Software License 2.0
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2012, Evan Summers
 */

package vellum.logr;

import vellum.logr.*;

/**
 *
 * @author evanx
 */
public class DequerProvider implements LogrProvider {
    LogrDispatcher dispatcher = new LogrDispatcher();
    DequerHandler dequerHandler = new DequerHandler();
    
    public DequerProvider() {
        dispatcher.getHandlerList().add(new DefaultHandler());
        dispatcher.getHandlerList().add(dequerHandler);
    }
    
    @Override
    public Logr getLogger(LogrContext context) {
        return new LogrAdapter(context, dispatcher);
    }

    public DequerHandler getDequerHandler() {
        return dequerHandler;
    }    
}
