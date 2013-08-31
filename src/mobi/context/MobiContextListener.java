
/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 * 
 */
package mobi.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import mobi.session.Servlets;


/**
 *
 * @author evan.summers
 */
public class MobiContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Servlets.contextInitialized(sce);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    
}
