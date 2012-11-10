/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2011, iPay (Pty) Ltd
 */
package vellum.log;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
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
