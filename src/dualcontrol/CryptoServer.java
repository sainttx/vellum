
package dualcontrol;

import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.util.Arrays;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class CryptoServer {
    static Logger logger = Logger.getLogger(CryptoServer.class);
    KeyStore keyStore;
    
    public static void main(String[] args) throws Exception {
        logger.info("args: " + Arrays.toString(args));
        if (args.length != 7) {
            System.err.println("usage: localAddress port backlog count remoteAddress keystore storepass");
        } else {
            new CryptoServer().run(InetAddress.getByName(args[0]), Integer.parseInt(args[1]), 
                    Integer.parseInt(args[2]), Integer.parseInt(args[3]), 
                    args[4], args[5], args[6].toCharArray());
        }
    }    
    
    private void run(InetAddress localAddress, int port, int backlog, int count, 
            String remoteHostAddress, String keyStoreName, char[] storepass) 
            throws Exception {
        logger.info(String.format("loading keystore %s", keyStoreName));
        keyStore = KeyStore.getInstance("JCEKS");
        if (keyStoreName.contains(":")) {
            String[] array = keyStoreName.split(":");
            Socket socket = DualControl.createSSLContext().getSocketFactory().
                createSocket(array[0], Integer.parseInt(array[1]));
            keyStore.load(socket.getInputStream(), storepass);
            socket.close();
        } else {
            keyStore.load(new FileInputStream(keyStoreName), storepass);
        }
        DualControl.init();
        ServerSocket serverSocket = DualControl.createSSLContext().getServerSocketFactory().
                createServerSocket(port, backlog, localAddress);
        while (true) {
            Socket socket = serverSocket.accept();
            logger.debug(socket.getInetAddress().getHostAddress());
            if (socket.getInetAddress().getHostAddress().equals(remoteHostAddress)) {
                new CryptoHandler().handle(keyStore, socket);
            }
            socket.close();
            if (count > 0 && --count == 0) break;
        }        
    }
}

