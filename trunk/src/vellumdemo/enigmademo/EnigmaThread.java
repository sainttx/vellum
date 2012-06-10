/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellumdemo.enigmademo;

import java.net.Socket;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan
 */
public class EnigmaThread extends Thread {
    static Logr logger = LogrFactory.getLogger(EnigmaThread.class);    
    EnigmaSocket enigmaSocket;
    
    public EnigmaThread(Socket clientSocket) {
        this.enigmaSocket = new EnigmaSocket(clientSocket);
    }
    
    public void run() {
        try {
            enigmaSocket.init();
            process();
        } catch (Exception e) {
            logger.warning(e, null);
        } finally {
            enigmaSocket.close();
        }
    }
    
    protected void process() throws Exception {
        String request = enigmaSocket.readObject(String.class);
        logger.info(request);
        enigmaSocket.writeObject("That's it man, game over man, game over!");
    }
}
