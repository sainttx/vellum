
package dualcontrol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
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
            String username = args[0];
            char[] password = args[1].toCharArray();
            try {
                Socket socket = DualControlSSLContextFactory.createSSLContext().getSocketFactory().
                        createSocket(HOST, PORT);
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                String alias = dis.readUTF();
                info("received prompt %s, sending %s %s",
                        alias, username, new String(password));
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                DualControlConsole.writeChars(dos, password);
                socket.close();
            } catch (Exception e) {
                System.out.println("ERROR send " + new String(password));
                throw e;
            }
        }
    }
    
    static void info(String format, Object ... args) {        
        System.err.println("INFO DummyDualControlClient: " + String.format(format, args));
        System.err.flush();
    }
        
}
