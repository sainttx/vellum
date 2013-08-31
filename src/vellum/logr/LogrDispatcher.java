/*
 * Apache Software License 2.0
 * Contributed (2013) by Evan Summers via https://code.google.com/p/vellum
 */
package vellum.logr;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author evan.summers
 */
public class LogrDispatcher implements LogrHandler {
    List<LogrHandler> handlerList = new CopyOnWriteArrayList();
    
    @Override
    public void handle(LogrContext context, LogrRecord message) {
        for (LogrHandler handler : handlerList) {
            handler.handle(context, message);
        }
    }

    public List<LogrHandler> getHandlerList() {
        return handlerList;
    }
 
}
