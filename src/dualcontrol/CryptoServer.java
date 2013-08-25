
package dualcontrol;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class CryptoServer {
    static Logger logger = Logger.getLogger(CryptoServer.class);
    static final String prompt = "CryptoServer";
    DualControlSession dualControlSession = new DualControlSession();
    
    public static void main(String[] args) throws Exception {
        logger.info("args: " + Arrays.toString(args));
        if (args.length != 7) {
            System.err.println(
                    "usage: localAddress port backlog count remoteAddress keyStore storePass");
        } else {
            new CryptoServer().run(InetAddress.getByName(args[0]), Integer.parseInt(args[1]), 
                    Integer.parseInt(args[2]), Integer.parseInt(args[3]), 
                    args[4], args[5], args[6].toCharArray());
        }
    }    
    
    private void run(InetAddress localAddress, int port, int backlog, int count, 
            String remoteHostAddress, String keyStoreLocation, char[] storePass) 
            throws Exception {
        dualControlSession.configure(keyStoreLocation, storePass, prompt);
        ServerSocket serverSocket = DualControlSSLContextFactory.createSSLContext().
                getServerSocketFactory().createServerSocket(port, backlog, localAddress);
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                logger.debug("remote " + socket.getInetAddress().getHostAddress());
                if (socket.getInetAddress().getHostAddress().equals(remoteHostAddress)) {
                    new CryptoHandler().handle(dualControlSession, socket);
                }
            } catch (Exception e) {
                logger.error("request handling error", e);
            } finally {
                close(socket);
            }
            if (count > 0 && --count == 0) break;
        }        
    }
    
    private static void close(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
                logger.warn("close socket", e);                
            }
        }
    }
}

