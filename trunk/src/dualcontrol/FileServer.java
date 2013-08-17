
package dualcontrol;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class FileServer {
    private static Logger logger = Logger.getLogger(FileServer.class);
    
    public static void main(String[] args) throws Exception {
        if (args.length != 6) {
            System.err.println("usage: localAddress port backlog count remoteAddress file");
        } else {
            new FileServer().run(InetAddress.getByName(args[0]), Integer.parseInt(args[1]), 
                    Integer.parseInt(args[2]), Integer.parseInt(args[3]), args[4], args[5]);
        }        
    }        

    private void run(InetAddress localAddress, int port, int backlog, int count, 
            String remoteHostAddress, String fileName) throws Exception {
        ServerSocket serverSocket = DualControl.createSSLContext().getServerSocketFactory().
                createServerSocket(port, backlog, localAddress);
        FileInputStream stream = new FileInputStream(fileName);
        int length = (int) new File(fileName).length();
        byte[] bytes = new byte[length];
        stream.read(bytes);
        while (true) {
            Socket socket = serverSocket.accept();
            logger.info("hostAddress " + socket.getInetAddress().getHostAddress());
            if (socket.getInetAddress().getHostAddress().equals(remoteHostAddress)) {
                socket.getOutputStream().write(bytes);
            }
            socket.close();
            if (count > 0 && --count == 0) break;
        }        
    }    
}
