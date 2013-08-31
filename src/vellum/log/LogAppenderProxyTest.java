/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF 2011, iPay (Pty) Ltd
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
