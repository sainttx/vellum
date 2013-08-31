/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0. 2011, iPay (Pty) Ltd
 */
package vellum.log;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 *
 * @author evan.summers
 */
public class LogAppenderProxyTest {

    public static final void main(String[] args) throws Exception {
        LogAppenderProxy proxy = new LogAppenderProxy();
        proxy.setJarFileName("/home/evans/cvs/bizmonger/dist/bizmonger.jar");
        proxy.setTargetClassName("bizmonger.GroovyMonAppender");
        proxy.setDebug(true);
        BasicConfigurator.configure(proxy);
        for (int i = 0; i < 20; i++) {
            Thread.sleep(2000);
            Logger.getRootLogger().info("" + i);
        }
    }
}
