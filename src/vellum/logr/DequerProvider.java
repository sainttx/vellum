/*
 * Apache Software License 2.0
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
 */

package vellum.logr;

import vellum.logr.*;

/**
 *
 * @author evan.summers
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
