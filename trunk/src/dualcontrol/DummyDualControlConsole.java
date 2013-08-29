/*
 * Apache Software License 2.0, (c) Copyright 2013, Evan Summers
 * 
 */

package dualcontrol;

import vellum.util.VellumProperties;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import javax.net.ssl.SSLContext;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public abstract class DummyDualControlConsole {

    final static Logger logger = Logger.getLogger(DummyDualControlConsole.class);
    final static int PORT = 4444;
    final static String HOST = "127.0.0.1";

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("usage: username passwd");
        } else {
            try {
                submit(DualControlSSLContextFactory.createSSLContext(
                        VellumProperties.systemProperties), 
                        args[0], args[1].toCharArray());
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }
    
    public static void submit(SSLContext sslContext, 
            String username, char[] password) throws Exception {
        Socket socket = sslContext.getSocketFactory().
                createSocket(HOST, PORT);
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        String purpose = dis.readUTF();
        Log.infof(logger, "submit password for %s from %s", purpose, username);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DualControlConsole.writeChars(dos, password);
        socket.close();
    }    
}
