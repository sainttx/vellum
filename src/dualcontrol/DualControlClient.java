
package dualcontrol;

import java.net.Socket;
import java.util.Arrays;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class DualControlClient {
    final static Logger logger = Logger.getLogger(DualControlClient.class);
    final static int PORT = 4444;
    final static String HOST = "127.0.0.1";

    public static void main(String[] args) throws Exception {
        char[] passwd = System.console().readPassword("DualControl password: ");
        Socket socket = DualControlKeyStores.createSSLContext().getSocketFactory().
                createSocket(HOST, PORT);
        byte[] bytes = getBytes(passwd);
        Arrays.fill(passwd, (char) 0);
        socket.getOutputStream().write(bytes);
        Arrays.fill(bytes, (byte) 0);
        socket.close();
    }
        
    public static byte[] getBytes(char[] chars) {
        byte[] array = new byte[chars.length];
        for (int i = 0; i < chars.length; i++) {
            array[i] = (byte) chars[i];    
        }
        return array;
    }               
}
